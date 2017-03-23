package com.example.admin.college20;

public class User {
    private String imageUrl, name, email, id;

    public User() {
    }

    public User(String imageUrl, String name, String email) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {this.email = email;}
}
