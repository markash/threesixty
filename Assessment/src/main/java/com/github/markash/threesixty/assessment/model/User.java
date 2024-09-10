package com.github.markash.threesixty.assessment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "user_detail")
public class User {
    @Id
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
