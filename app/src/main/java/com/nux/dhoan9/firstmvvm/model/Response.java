package com.nux.dhoan9.firstmvvm.model;

/**
 * Author: hoang
 * Created by: ModelGenerator on 03/07/2017
 */
public class Response {
    private String createTime;
    private String id;
    private String intent;
    private String state;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}