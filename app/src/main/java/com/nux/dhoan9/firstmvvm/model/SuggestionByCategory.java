package com.nux.dhoan9.firstmvvm.model;

import java.util.List;

/**
 * Created by hoang on 17/07/2017.
 */

public class SuggestionByCategory {
    private List<DishSugesstion> cutlery;
    private List<DishSugesstion> drinking;

    public List<DishSugesstion> getCutlery() {
        return cutlery;
    }

    public void setCutlery(List<DishSugesstion> cutlery) {
        this.cutlery = cutlery;
    }

    public List<DishSugesstion> getDrinking() {
        return drinking;
    }

    public void setDrinking(List<DishSugesstion> drinking) {
        this.drinking = drinking;
    }
}
