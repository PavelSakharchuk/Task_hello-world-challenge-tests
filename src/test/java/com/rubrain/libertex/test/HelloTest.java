package com.rubrain.libertex.test;

import com.github.javafaker.Faker;
import com.rubrain.libertex.Helper;
import com.rubrain.libertex.model.Client;
import com.rubrain.libertex.rest.Constants;
import com.rubrain.libertex.rest.responses.HelloResponse;
import com.rubrain.libertex.rest.responses.ResultCodeEnum;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HelloTest extends BaseTest {
    private String sessionId;
    private Client client;

    @BeforeAll
    public void preparation() {
        LOGGER.info("--- Preparation Hello test ---");
        Faker faker = new Faker();
        String fullName = faker.name().fullName();
        String userName = faker.name().username();

        this.client = new Client()
                .setFullName(fullName)
                .setUsername(userName);

        Helper.createClient(this.client);
        this.sessionId = Helper.login(this.client);
    }

    @Test
    void helloOkResponse200Test() {
        String expectedMessage = String.format("Hello, %s!", this.client.getFullName());

        HelloResponse expectedResponseBody = new HelloResponse()
                .setResultCode(ResultCodeEnum.OK)
                .setMessage(expectedMessage);

        HelloResponse helloResponse = given()
                .when()
                .header("X-Session-Id", this.sessionId)
                .get(Constants.GET_CHALLENGE_HELLO_URL)
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(HelloResponse.class);

        Assertions.assertEquals(expectedResponseBody, helloResponse);
    }

    @Test
    void helloEmptySessionId401Test() {
        HelloResponse expectedResponseBody = new HelloResponse()
                .setResultCode(ResultCodeEnum.UNAUTHORIZED);

        HelloResponse helloResponse = given()
                .when()
                .header("X-Session-Id", StringUtils.EMPTY)
                .get(Constants.GET_CHALLENGE_HELLO_URL)
                .then()
                .statusCode(401)
                .extract()
                .body().as(HelloResponse.class);

        Assertions.assertEquals(expectedResponseBody, helloResponse);
    }

    @Test
    void helloNotActualSessionId401Test() {
        HelloResponse expectedResponseBody = new HelloResponse()
                .setResultCode(ResultCodeEnum.UNAUTHORIZED);

        HelloResponse helloResponse = given()
                .when()
                .header("X-Session-Id", UUID.randomUUID().toString())
                .get(Constants.GET_CHALLENGE_HELLO_URL)
                .then()
                .statusCode(401)
                .extract()
                .body().as(HelloResponse.class);

        Assertions.assertEquals(expectedResponseBody, helloResponse);
    }

    @Test
    void helloWithoutSessionId401Test() {
        HelloResponse expectedResponseBody = new HelloResponse()
                .setResultCode(ResultCodeEnum.UNAUTHORIZED);

        HelloResponse helloResponse = given()
                .when()
                .get(Constants.GET_CHALLENGE_HELLO_URL)
                .then()
                .statusCode(401)
                .extract()
                .body().as(HelloResponse.class);

        Assertions.assertEquals(expectedResponseBody, helloResponse);
    }

    @Test
    void helloSessionIdInURL401Test() throws MalformedURLException, URISyntaxException {
        HelloResponse expectedResponseBody = new HelloResponse()
                .setResultCode(ResultCodeEnum.UNAUTHORIZED);

        URIBuilder builder = new URIBuilder();
        builder.setScheme("http");
        builder.setHost("localhost");
        builder.setPort(8080);
        builder.setPath(Constants.GET_CHALLENGE_HELLO_URL);
        builder.addParameter("X-Session-Id", sessionId);
        URL url = builder.build().toURL();

        HelloResponse helloResponse = given()
                .when()
                .get(url)
                .then()
                .statusCode(401)
                .extract()
                .body().as(HelloResponse.class);

        Assertions.assertEquals(expectedResponseBody, helloResponse);
    }

    @Test
    void helloBinaryContentType415Test() {
        String expectedResponseBody = StringUtils.EMPTY;

        String helloResponse = given()
                .when()
                .contentType(ContentType.BINARY)
                .header("X-Session-Id", this.sessionId)
                .get(Constants.GET_CHALLENGE_HELLO_URL)
                .then()
                .statusCode(415)
                .extract()
                .body().asString();

        Assertions.assertEquals(expectedResponseBody, helloResponse);
    }
}
