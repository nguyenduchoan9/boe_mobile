package com.nux.dhoan9.firstmvvm.data.error;

import com.google.gson.annotations.SerializedName;

import java.util.List;
/**
 * Created by hoang on 29/04/2017.
 */

public class RegisterError {

    private Error error;

    public Error getError() {
        return error;
    }

    public class Error {
        private Message message;
        private String code;
        private int status;

        public Message getMessage() {
            return message;
        }

        public String getCode() {
            return code;
        }

        public int getStatus() {
            return status;
        }
    }

    public class Message {
        private List<String> email;
        private List<String> password;
        @SerializedName("password_confirmation")
        private List<String> passwordConfirmation;

        public List<String> getEmail() {
            return email;
        }

        public List<String> getPassword() {
            return password;
        }

        public List<String> getPasswordConfirmation() {
            return passwordConfirmation;
        }
    }
}
