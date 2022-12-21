package com.bookit.utilities;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;

import static io.restassured.RestAssured.given;



public class BookItApiUtil {
    static Response response;
    static String path;

    public static String generateToken(String email, String password) {
        Response response =
                given()
                        .accept(ContentType.JSON)
                        .queryParams("email", email,
                                "password", password)
                        .when()
                        .get(ConfigurationReader.get("base_url") + "/sign");
        Assert.assertEquals(200, response.statusCode());
        String token= response.path("accessToken");
        String finalTOken = "Bearer "+ token;
        return finalTOken;
    }

    public static void deleteStudent(String studentEmail, String studentPassword){
        String studentToken = BookItApiUtil.generateToken(studentEmail, studentPassword);

        //get request
        response = given()
                .accept(ContentType.JSON)
                .and()
                .header("Authorization", studentToken)
                .when()
                .get(ConfigurationReader.get("base_url") + "/api/users/me");

        int currentUserId = response.jsonPath().getInt("id");
        System.out.println("currentUserId = " + currentUserId);


        //DELETE request
        String teacherToken = BookItApiUtil.generateToken(ConfigurationReader.get("teacher_email"),
                ConfigurationReader.get("teacher_password"));


        given().pathParam("id", currentUserId)
                .accept(ContentType.JSON)
                .and()
                .header("Authorization", teacherToken)
                .when()
                .delete(ConfigurationReader.get("base_url")+ "/api/students/{id}")
                .then()
                .statusCode(204);


        System.out.println("response.statusCode() = " + response.statusCode());
    }
}
