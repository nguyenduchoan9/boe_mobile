package com.nux.dhoan9.firstmvvm.manager;

import com.nux.dhoan9.firstmvvm.model.HeaderCredential;
import com.nux.dhoan9.firstmvvm.model.User;

import okhttp3.Headers;

/**
 * Created by hoang on 27/03/2017.
 */

public interface PreferencesManager {
    User getUser();
    void logIn(User user);
    boolean isLoggedin();
    void logOut();
    void saveCredentialHeader(Headers headers);
    HeaderCredential getCredentialHeader();
    int getRole();
    void setRole(int role);
}
