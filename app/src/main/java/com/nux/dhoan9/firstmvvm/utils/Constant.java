package com.nux.dhoan9.firstmvvm.utils;

/**
 * Created by hoang on 01/04/2017.
 */

public class Constant {
    public final static String INVALID_EMAIL_ERROR = "Invalid email";
    public final static String INVALID_PASSWORD_ERROR = "Invalid password";
    public final static String INVALID_PASSWORD_LENGTH_ERROR = "Password length must be in from 6 to 32 characters";
    public final static String INVALID_PASSWORD_NOT_MATCH_ERROR = "Password confirm is not match password";

    public final static String FIRST_NAME_PARAMS = "first_name";
    public final static String LAST_NAME_PARAMS = "last_name";
    public final static String EMAIL_PARAMS = "email";
    public final static String PASSWORD_PARAMS = "password";
    public final static String PASSWORD_CONFIRMATION_PARAMS = "password_confirmation";

    public final static String HEADER_ACCESS_TOKEN = "access-token";
    public final static String HEADER_EXPIRY = "expiry";
    public final static String HEADER_TOKEN_TYPE = "token-type";
    public final static String HEADER_UID = "uid";
    public final static String HEADER_CLIENT = "client";

    public final static int ROLE_CUSTOMER = 2;
    public final static int ROLE_CHEF = 2;

    public final static String SCANNER_RESULT_CONTENT = "SCANNER_RESULT_CONTENT";
    public final static String SCANNER_RESULT_QRCODE = "SCANNER_RESULT_QRCODE";

    public final static int MENU_TYPE_CART = 1;
    public final static int MENU_TYPE_HISTORY = 2;
}
