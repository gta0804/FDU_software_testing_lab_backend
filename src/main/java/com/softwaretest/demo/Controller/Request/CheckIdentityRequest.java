package com.softwaretest.demo.Controller.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CheckIdentityRequest {

    private String IDNumber;
    private String token;


    @Autowired
    public CheckIdentityRequest(){

    }

    public String getIDNumber() {
        return IDNumber;
    }

    public String getToken() {
        return token;
    }
}
