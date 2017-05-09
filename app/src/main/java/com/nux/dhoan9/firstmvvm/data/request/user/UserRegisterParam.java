package com.nux.dhoan9.firstmvvm.data.request.user;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by hoang on 29/04/2017.
 */

public class UserRegisterParam {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String password_confirmation;

    public UserRegisterParam(String firstName, String lastName,
                             String email, String password, String password_confirmation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }
}
