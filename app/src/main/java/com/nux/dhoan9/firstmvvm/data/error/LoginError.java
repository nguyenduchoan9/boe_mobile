package com.nux.dhoan9.firstmvvm.data.error;

import com.google.gson.annotations.SerializedName;

import java.util.List;
/**
 * Created by hoang on 02/05/2017.
 */

public class LoginError {
    private Error error;

    public Error getError() {
        return error;
    }

    public class Error {
        private List<Object> message;
        private String code;
        private int status;

        public List<Object> getMessage() {
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

    }
}
