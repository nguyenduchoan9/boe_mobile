package com.nux.dhoan9.firstmvvm.data.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hoang on 18/06/2017.
 */

public class CanOrder {
    @SerializedName("is_available")
    private boolean available;

    public boolean isAvailable() {
        return available;
    }
}
