package com.nux.dhoan9.firstmvvm.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.nux.dhoan9.firstmvvm.model.User;
import com.nux.dhoan9.firstmvvm.utils.StringUtils;

import okhttp3.Headers;

import com.nux.dhoan9.firstmvvm.model.HeaderCredential;

/**
 * Created by hoang on 27/03/2017.
 */

public class PreferencesManagerImpl implements PreferencesManager {
    private static String USER = "USER";
    private static String HEADERS = "HEADERS";
    private static String ROLE = "ROLE";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public PreferencesManagerImpl(Context context, Gson gson) {
        this.sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        this.gson = gson;
    }


    @Override
    public User getUser() {
        String userJSON = sharedPreferences.getString(USER, "");
        if (!StringUtils.isEmpty(userJSON)) {
            return gson.fromJson(userJSON, User.class);
        }
        return null;
    }

    @Override
    public void logIn(User user) {
        sharedPreferences.edit()
                .putString(USER, gson.toJson(user))
                .apply();
    }

    @Override
    public boolean isLoggedin() {
        return null != getUser();
    }

    @Override
    public void logOut() {
        sharedPreferences.edit()
                .remove(USER)
                .apply();
        sharedPreferences.edit()
                .remove(ROLE)
                .apply();
    }

    @Override
    public void saveCredentialHeader(Headers headerResponse) {
        String accessToken = headerResponse.get("access-token");
        String expiry = headerResponse.get("expiry");
        String tokenType = headerResponse.get("token-type");
        String uid = headerResponse.get("uid");
        String client = headerResponse.get("client");
        HeaderCredential headers = new HeaderCredential(accessToken,
                expiry, tokenType, uid, client);
        sharedPreferences.edit()
                .putString(HEADERS, gson.toJson(headers))
                .apply();
    }

    @Override
    public HeaderCredential getCredentialHeader() {
        String headersCredential = sharedPreferences.getString(HEADERS, "");
        if (!StringUtils.isEmpty(headersCredential)) {
            HeaderCredential headers = gson.fromJson(headersCredential, HeaderCredential.class);
            return headers;
        }
        return null;
    }

    @Override
    public int getRole() {
        return sharedPreferences.getInt(ROLE, -1);
    }

    @Override
    public void setRole(int role) {
        sharedPreferences.edit()
                .putInt(ROLE, role)
                .apply();
    }


}
