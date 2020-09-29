package com.rubrain.libertex.test;

import com.github.javafaker.Faker;
import com.rubrain.libertex.AfterTestExtension;
import com.rubrain.libertex.BeforeTestExtension;
import com.rubrain.libertex.Logger;
import com.rubrain.libertex.model.Client;
import com.rubrain.libertex.rest.body.LoginBody;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

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

        createClient(client);
        this.sessionId = login(client);
    }

    @Test
    public void hello200Test() {
        given()
                .when()
                .header("X-Session-Id", this.sessionId)
                .get("/challenge/hello")
                .then()
                .statusCode(200);
    }

    @Test
    public void hello401Test() {
        given()
                .when()
                .header("X-Session-Id", UUID.randomUUID().toString())
                .get("/challenge/hello")
                .then()
                .statusCode(401);
    }

    @Test
    public void hello500Test() {
        given()
                .when()
                .header("X-Session-Id", this.sessionId)
                .get("/challenge/hello")
                .then()
                .statusCode(500);
    }

    private void createClient(Client client) {
        LOGGER.info(String.format("--- Create Client: %s ---", client));

        given()
                .contentType(ContentType.JSON)
                .body(client)
                .post("/challenge/clients");
    }

    private String login(Client client) {
        LOGGER.info(String.format("--- Login Client: %s ---", client));

        LoginBody loginBody = new LoginBody()
                .setUsername(client.getUsername());

        return given()
                .contentType(ContentType.JSON)
                .body(loginBody)
                .post("/challenge/login")
                .header("X-Session-Id");
    }
}
