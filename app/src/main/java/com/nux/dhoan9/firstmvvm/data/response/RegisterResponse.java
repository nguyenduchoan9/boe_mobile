package com.nux.dhoan9.firstmvvm.data.response;

import com.nux.dhoan9.firstmvvm.model.Errors;
import com.nux.dhoan9.firstmvvm.model.User;

/**
 * Created by hoang on 27/03/2017.
 */

public class RegisterResponse {
    private boolean success;
    private Errors errors;
    private User user;

    public RegisterResponse() {
    }

    public RegisterResponse(boolean success, Errors errors, User user) {
        this.success = success;
        this.errors = errors;
        this.user = user;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
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
