package com.rubrain.libertex.test;

import com.github.javafaker.Faker;
import com.rubrain.libertex.AfterTestExtension;
import com.rubrain.libertex.BeforeTestExtension;
import com.rubrain.libertex.Helper;
import com.rubrain.libertex.Logger;
import com.rubrain.libertex.model.Client;
import com.rubrain.libertex.rest.responses.HelloResponse;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@ExtendWith({
        BeforeTestExtension.class,
        AfterTestExtension.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HelloTest {
    protected static final Logger LOGGER = Logger.getInstance();

    String sessionId;
    Client client;

    @BeforeAll
    public void preparation() {
        LOGGER.info("--- Preparation ---");
        Faker faker = new Faker();
        String fullName = faker.name().fullName();
        String userName = faker.name().username();

        this.client = new Client()
                .setFullName(fullName)
                .setUsername(userName);

        Helper.createClient(client);
        this.sessionId = Helper.login(client);
    }

    @Test
    public void helloOkResponse200Test() {
        String expectedMessage = String.format("Hello, %s!", this.client.getFullName());

        HelloResponse helloResponse = given()
                .when()
                .header("X-Session-Id", this.sessionId)
                .get("/challenge/hello")
                .then()
                .statusCode(200)
                .extract()
                .body().as(HelloResponse.class);

        HelloResponse expectedResponseBody = new HelloResponse()
                .setResultCode(HelloResponse.ResultCode.OK)
                .setMessage(expectedMessage);

        Assertions.assertEquals(helloResponse, expectedResponseBody);
    }

    @Test
    public void helloEmptySessionId401Test() {
        HelloResponse helloResponse = given()
                .when()
                .header("X-Session-Id", "")
                .get("/challenge/hello")
                .then()
                .statusCode(401)
                .extract()
                .body().as(HelloResponse.class);

        HelloResponse expectedResponseBody = new HelloResponse()
                .setResultCode(HelloResponse.ResultCode.UNAUTHORIZED);

        Assertions.assertEquals(helloResponse, expectedResponseBody);
    }

    @Test
    public void helloNotActualSessionId401Test() {
        HelloResponse helloResponse = given()
                .when()
                .header("X-Session-Id", UUID.randomUUID().toString())
                .get("/challenge/hello")
                .then()
                .statusCode(401)
                .extract()
                .body().as(HelloResponse.class);

        HelloResponse expectedResponseBody = new HelloResponse()
                .setResultCode(HelloResponse.ResultCode.UNAUTHORIZED);

        Assertions.assertEquals(helloResponse, expectedResponseBody);
    }

    @Test
    public void helloWithoutSessionId401Test() {
        HelloResponse helloResponse = given()
                .when()
                .get("/challenge/hello")
                .then()
                .statusCode(401)
                .extract()
                .body().as(HelloResponse.class);

        HelloResponse expectedResponseBody = new HelloResponse()
                .setResultCode(HelloResponse.ResultCode.UNAUTHORIZED);

        Assertions.assertEquals(helloResponse, expectedResponseBody);
    }

    @Test
    public void helloSessionIdInURL401Test() throws MalformedURLException, URISyntaxException {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http");
        builder.setHost("localhost");
        builder.setPort(8080);
        builder.setPath("/challenge/hello");
        builder.addParameter("X-Session-Id", sessionId);
        URL url = builder.build().toURL();

        HelloResponse helloResponse = given()
                .when()
                .get(url)
                .then()
                .statusCode(401)
                .extract()
                .body().as(HelloResponse.class);

        HelloResponse expectedResponseBody = new HelloResponse()
                .setResultCode(HelloResponse.ResultCode.UNAUTHORIZED);

        Assertions.assertEquals(helloResponse, expectedResponseBody);
    }
}
