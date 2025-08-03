package org.example;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class WireMockTests extends TestBase{

    private WireMockServer wireMockServer;


    @BeforeClass
    public void setupWireMock()
    {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        System.out.println("Wire mock server is running successfully");
    }

    @AfterClass
    public void tearDown()
    {
        if(wireMockServer.isRunning())
        {
            wireMockServer.stop();
            System.out.println("Wire mock server is stopped");
        }
    }

    @Test
    public void testmockedEnvironment001()
    {
        wireMockServer.stubFor(get(urlEqualTo("/products/123"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(content_type,json)
                        .withBody("{\"id\":123,\"name\":\"Mocked Product\",\"price\":49.99}")));

        given()
                .baseUri("http://localhost:8080")
                .log().all()
                .when()
                .get("/products/123")
                .then()
                .statusCode(200)
                .body("id",equalTo(123))
                .body("name",equalTo("Mocked Product"))
                .body("price",equalTo(49.99f))
                .log().all();
        System.out.println("Test passed: Successfully retrieved a mocked product.");
    }

    @Test
    public void testMockedEndpoint_shouldReturnNotFound() {
        // Step 1: Define a mock for a 404 Not Found scenario
        wireMockServer.stubFor(get(urlEqualTo("/products/404"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"message\": \"Product not found\" }")));

        // Step 2: Test against the 404 scenario
        given()
                .baseUri("http://localhost:8080")
                .log().all()
                .when()
                .get("/products/404")
                .then()
                .statusCode(404)
                .body("message", equalTo("Product not found"))
                .log().all();

        System.out.println("Test passed: Correctly handled a mocked 404 response.");
    }
}
