package com.example.cmaisonneuve;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String fullname;
    private String username;
    private String password;

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

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