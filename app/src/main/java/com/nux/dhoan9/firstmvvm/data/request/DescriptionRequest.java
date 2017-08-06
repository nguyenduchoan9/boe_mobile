package com.nux.dhoan9.firstmvvm.data.request;

import java.util.List;

/**
 * Created by hoang on 06/08/2017.
 */

public class DescriptionRequest {
    private int dishId;
    private List<String> description;

    public DescriptionRequest(int dishId, List<String> description) {
        this.dishId = dishId;
        this.description = description;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }
}
