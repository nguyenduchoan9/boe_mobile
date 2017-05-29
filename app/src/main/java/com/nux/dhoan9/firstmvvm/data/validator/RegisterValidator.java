package com.nux.dhoan9.firstmvvm.data.validator;

import android.content.res.Resources;

import com.nux.dhoan9.firstmvvm.utils.Constant;

import java.util.regex.Pattern;

/**
 * Created by hoang on 27/03/2017.
 */

public class RegisterValidator extends BaseValidator {
    private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private final String USERNAME = "USERNAME";
    private final String PASSWORD = "PASSWORD";
    private final String PASSWORDCONFIRM = "PASSWORDCONFIRM";

    public RegisterValidator(Resources resources) {
        super(resources);
    }

    public boolean validate() {
        return errors.isEmpty();
    }

    public String validateUsername(String username) {
        String error = null;
        if (username.trim().length() < 8 || username.trim().length() > 40) {
            error = Constant.INVALID_USERNAME_ERROR;
        }
        this.pushError(USERNAME, error);
        return error;
    }

    public String validatePassword(String password) {
        String error = null;
        if (password.trim().length() < 8 || password.trim().length() > 40) {
            error = Constant.INVALID_PASSWORD_LENGTH_ERROR;
        }
        this.pushError(PASSWORD, error);
        return error;
    }

    public String validatePasswordConfirm(String password, String passwordConfirm) {
        String error = null;
        if (passwordConfirm.trim().length() < 8 || passwordConfirm.trim().length() > 40) {
            error = Constant.INVALID_PASSWORD_LENGTH_ERROR;
        } else if (!password.equals(passwordConfirm)) {
            error = Constant.INVALID_PASSWORD_NOT_MATCH_ERROR;
        }
        this.pushError(PASSWORDCONFIRM, error);
        return error;
    }

    private void pushError(String errorKey, String error) {
        if (null != error) {
            this.errors.put(errorKey, error);
        } else {
            if (errors.containsKey(errorKey)) {
                errors.remove(errorKey);
            }
        }
    }
}
