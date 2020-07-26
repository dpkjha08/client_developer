package com.example.datascrapper.Model;
//import com.example.datascrapper.Model.UsersModel;

public class UsersModel {
    String email;
    String full_name;

    private UsersModel(){}

    private UsersModel(String email, String full_name){
        this.email = email;
        this.full_name = full_name;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public String getFull_name() {

        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
}
