package com.bookit.step_definitions;

import com.bookit.utilities.BookItApiUtil;
import com.bookit.utilities.ConfigurationReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ApiStepDefs {
    String token;
    Response response;
    @Given("I logged Bookit api using {string} and {string}")
    public void ı_logged_Bookit_api_using_and(String email, String password) {
        token = BookItApiUtil.generateToken(email, password);
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

//        System.out.println("firstNameFromAPI = " + firstNameFromAPI);
//        System.out.println("lastNameFromAPI = " + lastNameFromAPI);
//        System.out.println("idFromAPI = " + idFromAPI);
//        System.out.println("roleFromAPI = " + roleFromAPI);
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

        //Assertion
        Assert.assertEquals(idFromAPI, infoFromDB.get("id"));
        Assert.assertEquals(firstNameFromAPI, infoFromDB.get("firstname"));
        Assert.assertEquals(lastNameFromAPI, infoFromDB.get("lastname"));
        Assert.assertEquals(roleFromAPI, infoFromDB.get("role"));
//        Assertions.assertAll(
//                ()-> assertEquals(idFromAPI, infoFromDB.get("id")),
//                ()-> assertEquals(firstNameFromAPI, infoFromDB.get("firstname")),
//                ()-> assertEquals(lastNameFromAPI, infoFromDB.get("lastname")),
//                ()-> assertEquals(roleFromAPI, infoFromDB.get("role"))
//        );

    }
}
