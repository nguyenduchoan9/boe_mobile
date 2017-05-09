package com.nux.dhoan9.firstmvvm.data.response;

import com.nux.dhoan9.firstmvvm.model.User;

/**
 * Created by hoang on 27/03/2017.
 */

public class LoginResponse {
    private boolean success;
    private User user;

    public LoginResponse(boolean success, User user) {
        this.success = success;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
