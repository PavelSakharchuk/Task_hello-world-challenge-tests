package com.rubrain.libertex;

import com.rubrain.libertex.model.Client;
import com.rubrain.libertex.rest.body.LoginBody;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class Helper {
    protected static final Logger LOGGER = Logger.getInstance();

    public static void createClient(Client client) {
        LOGGER.info(String.format("--- Create Client: %s ---", client));

        given()
                .contentType(ContentType.JSON)
                .body(client)
                .post("/challenge/clients");
    }

    public static String login(Client client) {
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
