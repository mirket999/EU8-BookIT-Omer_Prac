package com.bookit.step_definitions;

import com.bookit.utilities.BookItApiUtil;
import com.bookit.utilities.ConfigurationReader;
import com.bookit.utilities.Environment;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ApiStepDefs {
    String token;
    Response response;
    String studentPassword;
    String studentEmail;
    @Given("I logged Bookit api using {string} and {string}")
    public void ı_logged_Bookit_api_using_and(String email, String password) {
        token = BookItApiUtil.generateToken(email, password);
        System.out.println("token = " + token);
    }

    @When("I get the current user information from api")
    public void ı_get_the_current_user_information_from_api() {
        response = given()
                .accept(ContentType.JSON)
                .and()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(ConfigurationReader.get("base_url") + "/api/users/me");
    }

    @Then("status code should be {int}")
    public void status_code_should_be(int statusCode) {
        Assert.assertEquals(statusCode, response.statusCode());
    }

    @Then("user information from api and database should match")
    public void user_information_from_api_and_database_should_match() throws SQLException {
    //from api
        String idFromAPI = response.jsonPath().getString("id");
        String firstNameFromAPI = response.jsonPath().getString("firstName");
        String lastNameFromAPI = response.jsonPath().getString("lastName");
        String roleFromAPI = response.jsonPath().getString("role");

        System.out.println("firstNameFromAPI = " + firstNameFromAPI);
        System.out.println("lastNameFromAPI = " + lastNameFromAPI);
        System.out.println("idFromAPI = " + idFromAPI);
        System.out.println("roleFromAPI = " + roleFromAPI);
        //from DB
        String URL = "jdbc:postgresql://room-reservation-qa2.cxvqfpt4mc2y.us-east-1.rds.amazonaws.com:5432/room_reservation_qa2";
        String userName = "qa_user";
        String password = "Cybertek11!";
        Connection connection = DriverManager.getConnection(URL, userName, password);
        Statement statement = connection.createStatement(1004, 1007);
        ResultSet resultSet = statement.executeQuery("select id, firstname, lastname, role from users\n" +
                "where id = 1202");
        ResultSetMetaData rsmd = resultSet.getMetaData();

        resultSet.next();
        Map<String, String> infoFromDB = new LinkedHashMap<>();

        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            infoFromDB.put(rsmd.getColumnName(i), resultSet.getString(i));
        }

        System.out.println("infoFromDB = " + infoFromDB);

        //Assertion with cucumber-junit
//        Assert.assertEquals(idFromAPI, infoFromDB.get("id"));
//        Assert.assertEquals(firstNameFromAPI, infoFromDB.get("firstname"));
//        Assert.assertEquals(lastNameFromAPI, infoFromDB.get("lastname"));
//        Assert.assertEquals(roleFromAPI, infoFromDB.get("role"));

        //Assertion with jUnit 5
        Assertions.assertAll(
                ()-> assertEquals(idFromAPI, infoFromDB.get("id")),
                ()-> assertEquals(firstNameFromAPI, infoFromDB.get("firstname")),
                ()-> assertEquals(lastNameFromAPI, infoFromDB.get("lastname")),
                ()-> assertEquals(roleFromAPI, infoFromDB.get("role"))
        );

    }

    @When("I send POST request to {string} endpoint with following information")
    public void ıSendPOSTRequestToEndpointWithFollowingInformation(String path, Map<String, String> studentInfo) {
    studentEmail = studentInfo.get("email");
    studentPassword = studentInfo.get("password");
        response= given().accept(ContentType.JSON).queryParams(studentInfo)
            .log().all()
            .and().header("Authorization", token)
            .when().post(ConfigurationReader.get("base_url")+path);
        System.out.println("response.statusCode() = " + response.statusCode());

    }


    @And("I delete previously added student")
    public void ıDeletePreviouslyAddedStudent() {
        //        token = BookItApiUtil.generateToken("hasan7@yahoo.com", "abc123");
//        System.out.println("token = " + token);
//
//        //get request
//        response = given()
//                .accept(ContentType.JSON)
//                .and()
//                .header("Authorization", token)
//                .when()
//                .get(ConfigurationReader.get("base_url") + "/api/users/me");
//
//        int currentUserId = response.jsonPath().getInt("id");
//        System.out.println("currentUserId = " + currentUserId);
//
//
//        //DELETE request
//        token = BookItApiUtil.generateToken(ConfigurationReader.get("teacher_email"),
//                ConfigurationReader.get("teacher_password"));
//
//        given().pathParam("id", currentUserId)
//                .accept(ContentType.JSON)
//                .and()
//                .header("Authorization", token)
//                .when()
//                .delete(ConfigurationReader.get("base_url")+ path)
//                .then()
//                .statusCode(204);
//
//
//        System.out.println("response.statusCode() = " + response.statusCode());

        BookItApiUtil.deleteStudent(studentEmail, studentPassword);
    }
}
