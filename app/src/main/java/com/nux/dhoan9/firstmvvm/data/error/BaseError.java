package com.nux.dhoan9.firstmvvm.data.error;

/**
 * Created by hoang on 02/05/2017.
 */

public class BaseError {
    private RegisterError.Error error;

    public RegisterError.Error getError() {
        return error;
    }

    public class Error {
        private RegisterError.Message message;
        private String code;
        private int status;

        public RegisterError.Message getMessage() {
            return message;
        }

        public String getCode() {
            return code;
        }

        public int getStatus() {
            return status;
        }
    }
}
