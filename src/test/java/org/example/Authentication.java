package org.example;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class Authentication extends TestBase{

    @Test
    public void token_generation()
    {
        String dummyJsonBaseUri = "https://dummyjson.com";
        String bearerToken = "";

        LoginRequest newUser = new LoginRequest("emilys","emilyspass");


        LoginResponse response = given()
                .baseUri(dummyJsonBaseUri)
                 .header(content_type,json)
                 .body(newUser)
                .log().all()
                .when()
                 .post("/auth/login")
                 .then()
                 .statusCode(200)
                 .body("accessToken",notNullValue())
                 .extract().as(LoginResponse.class);

        bearerToken = response.getToken();
        System.out.println("Token is: "+bearerToken);

        User userProfile = given()
                .baseUri(dummyJsonBaseUri)
                .header(content_type,json)
                .header("Authorization","Bearer"+bearerToken)
                .when()
                .get("/auth/me")
                .then()
                .statusCode(200)
                .body("username",equalTo("emilys"))
                .body("email",notNullValue())
                .log().all()
                .extract().as(User.class);

    }

}
