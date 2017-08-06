package com.nux.dhoan9.firstmvvm.manager;

import com.nux.dhoan9.firstmvvm.model.HeaderCredential;
import com.nux.dhoan9.firstmvvm.model.QRCodeTableInfo;
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
    String getRole();
    void setTableInfo(QRCodeTableInfo tableInfo);
    QRCodeTableInfo getTableInfo();
    String getLanguage();
    void setLanguage(String language);
    void setBalance(float balance);
}
