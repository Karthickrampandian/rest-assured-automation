package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {
    @JsonProperty("accessToken")
    private String token;

    public String getToken()
    {
       return this.token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }
}
