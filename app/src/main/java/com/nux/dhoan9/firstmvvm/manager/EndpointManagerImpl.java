package com.nux.dhoan9.firstmvvm.manager;

import android.content.Context;
import android.content.SharedPreferences;
import com.nux.dhoan9.firstmvvm.BuildConfig;
import com.nux.dhoan9.firstmvvm.utils.Constant;

/**
 * Created by hoang on 27/05/2017.
 */

public class EndpointManagerImpl implements EndpointManager {
    public static final String ENDPOINT = "ENDPOINT";
    private SharedPreferences sharedPreferences;

    public EndpointManagerImpl(Context context) {
        this.sharedPreferences = context.getSharedPreferences(
                Constant.ID, Context.MODE_PRIVATE);
    }

    @Override
    public String getEndpoint() {
        if (BuildConfig.IS_PROD) {
            return BuildConfig.BASE_URL;
        }
        Constant.API_ENDPOINT = sharedPreferences.getString(ENDPOINT, Constant.API_ENDPOINT);
        return Constant.API_ENDPOINT;
    }

    @Override
    public void setEndpoint(String endpoint) {
        Constant.API_ENDPOINT = endpoint;
        sharedPreferences.edit()
                .putString(ENDPOINT, endpoint)
                .apply();
    }
}
