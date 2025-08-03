package org.example;

import java.util.List;

public class SingleUserResponse
{
   private User data;
   private Support support;

    // Getters and Setters are required for deserialization
    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

    public Support getSupport() {
        return support;
    }

    public void setSupport(Support support) {
        this.support = support;
    }

}
