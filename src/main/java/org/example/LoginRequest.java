package org.example;

// Import Jackson annotations for JSON serialization
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {

    // Use the @JsonProperty annotation to specify the exact JSON key
    // This is a robust way to handle JSON serialization and deserialization
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    // Default constructor is required for some libraries like Jackson
    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters for the fields
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
