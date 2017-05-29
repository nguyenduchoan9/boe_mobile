package com.nux.dhoan9.firstmvvm.data.request.user;

import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hoang on 29/04/2017.
 */

public class UserRegisterParam {
    @SerializedName("full_name")
    public final String fullName;
    public final String username;
    public final String password;
    public final String password_confirmation;

    public UserRegisterParam(String fullName, String username, String password, String password_confirmation) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }
}
