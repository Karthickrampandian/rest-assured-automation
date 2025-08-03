package org.example;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestBase {
    public static final String api_key = "x-api-key";
    public static final String api_key_value = "reqres-free-v1";
    public static final String content_type = "Content-type";
    public static final String json = "application/json";

    @BeforeClass
    public void setup() {
        Properties properties = new Properties();
        try{
            FileInputStream fileInputStream = new FileInputStream("src/test/resources/config.properties");
            properties.load(fileInputStream);
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
        RestAssured.baseURI  = properties.getProperty("base.uri");

    }
}
