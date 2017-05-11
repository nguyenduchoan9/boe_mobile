package com.nux.dhoan9.firstmvvm.model;

/**
 * Created by hoang on 10/05/2017.
 */

public class ActionMenuHorizontal {
    public String title;
    public int type;

    public ActionMenuHorizontal(String title, int type) {
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
