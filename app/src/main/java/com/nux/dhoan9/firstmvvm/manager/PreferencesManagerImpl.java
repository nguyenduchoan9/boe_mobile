package com.nux.dhoan9.firstmvvm.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.nux.dhoan9.firstmvvm.BuildConfig;
import com.nux.dhoan9.firstmvvm.model.QRCodeTableInfo;
import com.nux.dhoan9.firstmvvm.model.User;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.utils.StringUtils;

import okhttp3.Headers;

import com.nux.dhoan9.firstmvvm.model.HeaderCredential;

/**
 * Created by hoang on 27/03/2017.
 */

public class PreferencesManagerImpl implements PreferencesManager {
    private static final String USER = "USER";
    private static final String HEADERS = "HEADERS";
    private static final String TABLE_INFO = "TABLE_INFO";
    private static final String LANGUAGE_INFO = "LANGUAGE_INFO";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public PreferencesManagerImpl(Context context, Gson gson) {
        this.sharedPreferences = context
                .getSharedPreferences(Constant.ID, Context.MODE_PRIVATE);
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
                .remove(TABLE_INFO)
                .apply();
        sharedPreferences.edit()
                .remove(HEADERS)
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
    public String getRole() {
        return getUser().getRole();
    }

    @Override
    public void setTableInfo(QRCodeTableInfo tableInfo) {
        sharedPreferences.edit()
                .putString(TABLE_INFO, gson.toJson(tableInfo))
                .apply();
    }

    @Override
    public QRCodeTableInfo getTableInfo() {
        if(BuildConfig.IS_FAKE){
            QRCodeTableInfo info = new QRCodeTableInfo("", String.valueOf(BuildConfig.NUMBER_TABLE));
            return info;
        }
        String userJSON = sharedPreferences.getString(TABLE_INFO, "");
        if (!StringUtils.isEmpty(userJSON)) {
            return gson.fromJson(userJSON, QRCodeTableInfo.class);
        }
        return null;
    }

    @Override
    public String getLanguage() {
        Log.d("Hoang", "getLanguage: " + sharedPreferences.getString(LANGUAGE_INFO, Constant.VI_LANGUAGE_STRING));
        return sharedPreferences.getString(LANGUAGE_INFO, Constant.VI_LANGUAGE_STRING);
    }

    @Override
    public void setLanguage(String language) {
        sharedPreferences.edit()
                .putString(LANGUAGE_INFO, language)
                .apply();
    }

}
