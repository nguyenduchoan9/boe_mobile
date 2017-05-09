package com.nux.dhoan9.firstmvvm.model;

import java.util.List;

/**
 * Created by hoang on 27/03/2017.
 */

public class Errors {
    public List<String> errors;

    public Errors() {
    }

    public Errors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
