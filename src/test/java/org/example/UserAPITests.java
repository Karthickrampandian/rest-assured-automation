package org.example;

import com.fasterxml.jackson.databind.util.JSONPObject;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import org.json.JSONObject;

@Epic("API Testing Suite")
@Feature("Trying out all HTTPS methods from rest assured")
public class UserAPITests extends TestBase{


    @Test
    public void getSingleUser()
    {
            given()
                    .header(api_key, api_key_value)
            .when()
                    .get("/api/users/2")
            .then()
                    .statusCode(200)
                    .log()
                    .all();
    }

    @Test
    @Story("Creating a new User")
    @Description("This test verified creation of new user  using post method")
    public void createNewUser()
    {

        Map<String, String> newUser = new HashMap<>();
        newUser.put("name","neo");
        newUser.put("job","the one");

        given()
                .header(content_type,json)
                .header(api_key,api_key_value)
                .body(newUser)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("name",equalTo("neo"))
//                .body("job",equalTo("the one"))
                .body("id",notNullValue())
                .log().all();
    }

    @Test
    @Story("Updating a existing User")
    @Description("This test verifies updating user information using put method")
    public void updateNewUser()
    {
        Map<String, String> newUser = new HashMap<>();
        newUser.put("name","neo");
        newUser.put("job","the one");

        given()
                .header(content_type,json)
                .header(api_key,api_key_value)
                .body(newUser)
                .when()
                .put("/api/users/962")
                .then()
                .statusCode(200)
                .body("name",equalTo("neo"))
                .body("updatedAt",notNullValue())
                .log().all();
    }

        @Test()
        @Story("Deleting a existing User")
        @Description("This test verifies deletion of new user using delete method")
        public void deleteuser()
        {
            given().header(api_key,api_key_value)
                    .when()
                    .delete("/api/users/1")
                    .then()
                    .statusCode(204)
//                    .body()
                    .log().all();
        }

        @Test
        @Story("Get all users details")
        @Description("This test helps to verify whether GET is working as expected")
    public void getListOfUsers()
        {
            UserResponse response =
                    given()
                            .header(api_key,api_key_value)
                            .when()
                            .get("/api/users")
                            .then()
                            .statusCode(200)
                            .log().all()
                            .extract()
                            .as(UserResponse.class);
            assertNotNull(response);
            assertEquals(response.getPage(),1);
            assertEquals(response.getData().size(),6);
            assertEquals(response.getData().get(0).getFirst_name(),"George");
        }

    @Test
    @Story("Requests chaining")
    @Description("This test is created to test chaining of requests")
    public void end_to_end_test001()
    {

        Map<String,String> createUser = new HashMap<>();

        String id = given()
                .header(api_key,api_key_value)
                .header(content_type,json)
                .body(createUser)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("id",notNullValue())
                .extract().path("id");

        System.out.println("Newly created User ID: "+id);

        SingleUserResponse userResponse = given()
                .header(api_key,api_key_value)
                .when()
                .get("/api/users/4")
                .then()
                .log().all()
                .extract().as(SingleUserResponse.class);

        given()
                .header(api_key,api_key_value)
                .when()
                .delete("/api/users/" + id)
                .then()
                .statusCode(204)
                .log().all();

        assertEquals(userResponse.getData().getId(),4);
        assertEquals(userResponse.getData().getEmail(),"eve.holt@reqres.in");
        assertEquals(userResponse.getData().getFirst_name(),"Eve");
    }

    @Test
    @Story("validating Json schema")
    @Description("This test is used to do Schema validation")
    public void schemaValidation()
    {
        given()
                .header(api_key,api_key_value)
                .when()
                .get("/api/users?pages=2")
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("users-schema.json"));
    }

    @Test(dataProvider = "userData")
    @Story("Test with data provider")
    @Description("This test verify data provider integration with our create requests")
    public void postWithDataProvider(String name, String job)
    {
        JSONObject newUser = new JSONObject();
        newUser.put("name",name);
        newUser.put("job",job);

        given()
                .header(content_type,json)
                .header(api_key,api_key_value)
                .body(newUser.toString())
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("name",equalTo(name))
                .body("job",equalTo(job))
                .log().all();

    }

    @Test
    @Story("Checking Query param")
    @Description("This test query param")
    public void getListOfUsersWithQueryParam() {
        given()
                .header(api_key,api_key_value)
                .queryParam("page", 2)
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .body("page", equalTo(2))
                .log().all();
    }

    @Test(dataProvider = "userIDs")
    @Story("Checking path param")
    @Description("This test path param")
    public void getUserWithPathParam_shouldReturnCorrectUser(int id, String firstName, String lastName) {
         given()
                .header(api_key, api_key_value)
                .pathParam("userId", id)
                .when()
                .get("/api/users/{userId}")
                .then()
                .statusCode(200)
                .body("data.id",equalTo(id))
                .body("data.first_name",equalTo(firstName))
                .body("data.last_name",equalTo(lastName))
                .log().all();
    }

    @DataProvider(name="userData")
    public Object[][] userData()
    {
        return new Object[][]
                {
                        {"neo","The One"},
                        {"Trinity","Hacker"},
                        {"Morpheus","Captian"},
                        {"Agent Smith","Program"}
                };
    }

    @DataProvider(name = "userIDs")
    public Object[][] userIDs() {
        return new Object[][] {
                {1, "George", "Bluth"},
                {2, "Janet", "Weaver"},
                {3, "Emma", "Wong"}
        };
    }
}
