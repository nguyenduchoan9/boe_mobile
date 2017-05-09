package com.nux.dhoan9.firstmvvm.model;

import com.google.gson.annotations.SerializedName;
/**
 * Created by hoang on 29/04/2017.
 */

public class HeaderCredential {
//    @SerializedName("access-token")
    private String accesToken;
    private String expiry;
//    @SerializedName("token-type")
    private String tokenType;
    private String uid;
    private String client;

    public HeaderCredential(String accesToken, String expiry, String tokenType, String uid, String client) {
        this.accesToken = accesToken;
        this.expiry = expiry;
        this.tokenType = tokenType;
        this.uid = uid;
        this.client = client;
    }

    public String getAccesToken() {
        return accesToken;
    }

    public String getExpiry() {
        return expiry;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getUid() {
        return uid;
    }

    public String getClient() {
        return client;
    }
}
