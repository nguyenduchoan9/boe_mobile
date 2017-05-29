package com.nux.dhoan9.firstmvvm.data.request.user;

/**
 * Created by hoang on 02/05/2017.
 */

public class LoginUserParam {
    public final String username;
    public final String password;

    public LoginUserParam(String email, String password) {
        this.username = email;
        this.password = password;
    }
}
