package com.nux.dhoan9.firstmvvm.data.request.user;

/**
 * Created by hoang on 02/05/2017.
 */

public class LoginUserParam {
    private String email;
    private String password;

    public LoginUserParam(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
