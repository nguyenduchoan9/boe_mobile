package com.nux.dhoan9.firstmvvm.data.validator;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hoang on 27/03/2017.
 */

public class BaseValidator {
    private Resources resources;
    protected Map<String, String> errors;

    public BaseValidator(Resources resources) {
        this.resources = resources;
        this.errors = new HashMap<>();
    }

    protected String getString(int id) {
        return resources.getString(id);
    }
}
