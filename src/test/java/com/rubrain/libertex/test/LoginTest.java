package com.rubrain.libertex.test;

import com.github.javafaker.Faker;
import com.rubrain.libertex.Helper;
import com.rubrain.libertex.model.Client;
import com.rubrain.libertex.rest.Constants;
import com.rubrain.libertex.rest.body.LoginBody;
import com.rubrain.libertex.rest.responses.LoginResponse;
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
class LoginTest extends BaseTest {
    private Client client;

    @BeforeAll
    public void preparation() {
        LOGGER.info("--- Preparation for Login Test ---");

        Faker faker = new Faker();
        String fullName = faker.name().fullName();
        String userName = faker.name().username();

        this.client = new Client()
                .setFullName(fullName)
                .setUsername(userName);

        Helper.createClient(this.client);
    }

    @Test
    void loginOkResponse200Test() {
        LoginResponse expectedLoginBody = new LoginResponse()
                .setResultCode(ResultCodeEnum.OK);

        LoginBody loginBody = new LoginBody()
                .setUsername(this.client.getUsername());

        LoginResponse loginResponse = given()
                .when()
                .contentType(ContentType.JSON)
                .body(loginBody)
                .post(Constants.POST_CHALLENGE_LOGIN_URL)
                .then()
                .statusCode(200)
                .extract()
                .body().as(LoginResponse.class);

        Assertions.assertEquals(expectedLoginBody, loginResponse);
    }

    @Test
    void loginOkHeader200Test() {
        LoginResponse expectedLoginBody = new LoginResponse()
                .setResultCode(ResultCodeEnum.OK);

        LoginBody loginBody = new LoginBody()
                .setUsername(this.client.getUsername());

        String sessionId = given()
                .when()
                .contentType(ContentType.JSON)
                .body(loginBody)
                .post(Constants.POST_CHALLENGE_LOGIN_URL)
                .then()
                .statusCode(200)
                .extract()
                .header("X-Session-Id");

        Assertions.assertNotNull(sessionId);
    }

    @Test
    void loginEmptyUserName500Test() {
        LoginResponse expectedLoginBody = new LoginResponse()
                .setResultCode(ResultCodeEnum.INCORRECT_PARAMETER)
                .setErrorMessage("Username or Password are not correct");

        LoginBody loginBody = new LoginBody()
                .setUsername(StringUtils.EMPTY);

        LoginResponse loginResponse = given()
                .when()
                .contentType(ContentType.JSON)
                .body(loginBody)
                .post(Constants.POST_CHALLENGE_LOGIN_URL)
                .then()
                .statusCode(500)
                .extract()
                .body().as(LoginResponse.class);

        Assertions.assertEquals(expectedLoginBody, loginResponse);
    }

    @Test
    void loginNotActualUserName500Test() {
        LoginResponse expectedLoginBody = new LoginResponse()
                .setResultCode(ResultCodeEnum.INCORRECT_PARAMETER)
                .setErrorMessage("Username or Password are not correct");

        LoginBody loginBody = new LoginBody()
                .setUsername(UUID.randomUUID().toString());

        LoginResponse loginResponse = given()
                .when()
                .contentType(ContentType.JSON)
                .body(loginBody)
                .post(Constants.POST_CHALLENGE_LOGIN_URL)
                .then()
                .statusCode(500)
                .extract()
                .body().as(LoginResponse.class);


        Assertions.assertEquals(expectedLoginBody, loginResponse);
    }

    @Test
    void loginNotActualUserNameHeader500Test() {
        LoginResponse expectedLoginBody = new LoginResponse()
                .setResultCode(ResultCodeEnum.INCORRECT_PARAMETER)
                .setErrorMessage("Username or Password are not correct");

        LoginBody loginBody = new LoginBody()
                .setUsername(UUID.randomUUID().toString());

        LoginResponse loginResponse = given()
                .when()
                .contentType(ContentType.JSON)
                .body(loginBody)
                .post(Constants.POST_CHALLENGE_LOGIN_URL)
                .then()
                .statusCode(500)
                .extract()
                .body().as(LoginResponse.class);


        Assertions.assertEquals(expectedLoginBody, loginResponse);
    }

    @Test
    void loginWithoutUserName500Test() {
        LoginResponse expectedLoginBody = new LoginResponse()
                .setResultCode(ResultCodeEnum.UNEXPECTEDERROR)
                .setErrorMessage("org.hibernate.id.IdentifierGenerationException: ids for this class must be manually assigned before calling save(): com.libertex.qa.challenge.model.Session");

        LoginResponse loginResponse = given()
                .when()
                .contentType(ContentType.JSON)
                .body(new LoginBody())
                .post(Constants.POST_CHALLENGE_LOGIN_URL)
                .then()
                .statusCode(500)
                .extract()
                .body().as(LoginResponse.class);


        Assertions.assertEquals(expectedLoginBody, loginResponse);
    }

    @Test
    void loginUserNameInURL415Test() throws MalformedURLException, URISyntaxException {
        String expectedLoginBody = StringUtils.EMPTY;

        URIBuilder builder = new URIBuilder();
        builder.setScheme("http");
        builder.setHost("localhost");
        builder.setPort(8080);
        builder.setPath(Constants.POST_CHALLENGE_LOGIN_URL);
        builder.addParameter("username", this.client.getUsername());
        URL url = builder.build().toURL();

        String loginResponse = given()
                .when()
                .post(url)
                .then()
                .statusCode(415)
                .extract()
                .body().asString();

        Assertions.assertEquals(expectedLoginBody, loginResponse);
    }

    @Test
    void loginWithoutJsonType415Test() {
        String expectedLoginBody = StringUtils.EMPTY;

        LoginBody loginBody = new LoginBody()
                .setUsername(this.client.getUsername());

        String loginResponse = given()
                .when()
                .body(loginBody)
                .post(Constants.POST_CHALLENGE_LOGIN_URL)
                .then()
                .statusCode(415)
                .extract()
                .body().asString();

        Assertions.assertEquals(expectedLoginBody, loginResponse);
    }
}
