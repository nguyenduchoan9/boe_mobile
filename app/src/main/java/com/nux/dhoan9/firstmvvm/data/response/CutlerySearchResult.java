package com.nux.dhoan9.firstmvvm.data.response;

import com.google.gson.annotations.SerializedName;
import com.nux.dhoan9.firstmvvm.model.MenuCategories;
import java.util.List;

/**
 * Created by hoang on 28/07/2017.
 */

public class CutlerySearchResult {
    @SerializedName("is_no_result")
    private boolean isNoResult;
    @SerializedName("result")
    private List<MenuCategories> result;

    public boolean isNoResult() {
        return isNoResult;
    }

    public List<MenuCategories> getResult() {
        return result;
    }
}
