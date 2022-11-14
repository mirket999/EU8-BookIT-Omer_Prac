package com.bookit.utilities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BookItApiUtil {
    public static String generateToken(String email, String password) {
        Response response =
                given()
                        .accept(ContentType.JSON)
                        .queryParams("email", email,
                                "password", password)
                        .when()
                        .get(ConfigurationReader.get("base_url") + "/sign");
        String token= response.path("accessToken");
        String finalTOken = "Bearer "+ token;
        return finalTOken;
    }
}
