package com.nux.dhoan9.firstmvvm.data.validator;

import android.content.res.Resources;

import com.nux.dhoan9.firstmvvm.utils.Constant;

import java.util.regex.Pattern;

/**
 * Created by hoang on 27/03/2017.
 */

public class LoginValidator extends BaseValidator {
    private final String EMAIL_ERROR_KEY = "EMAIL_ERROR_KEY";
    private final String PASSWORD_ERROR_KEY = "PASSWORD_ERROR_KEY";
    private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public LoginValidator(Resources resources) {
        super(resources);
    }

    public boolean isValidate() {
        return errors.isEmpty();
    }

    public String validateUsername(String email) {
        String error = null;
//        if(email.trim().length() < 8 || email.trim().length() > 40){
//            error = Constant.INVALID_USERNAME_ERROR;
//        }
//        pushError(EMAIL_ERROR_KEY, error);
        return error;
    }

    public String validatePassword(String password) {
        String error = null;
//        if (password.trim().length() < 6 || password.trim().length() > 40) {
//            error = Constant.INVALID_PASSWORD_LENGTH_ERROR;
//        }
//        pushError(PASSWORD_ERROR_KEY, error);
        return error;
    }

    private void pushError(String errorKey, String error) {
        if (null != error) {
            errors.put(errorKey, error);
        } else {
            if (errors.containsKey(errorKey)) {
                errors.remove(errorKey);
            }
        }
    }
}
