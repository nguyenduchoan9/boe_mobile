package com.nux.dhoan9.firstmvvm.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hoang on 27/03/2017.
 */

public class User {
    private int id;
    @SerializedName("full_name")
    private String fullName;
    private String avatar;
//    private Image avatar;
    private String phone;
    private String role;
    private String provider;
    private String username;
    private String membership;

    public User(int id, String fullName, String avatar, String phone, String role, String provider, String username, String membership) {
        this.id = id;
        this.fullName = fullName;
        this.avatar = avatar;
        this.phone = phone;
        this.role = role;
        this.provider = provider;
        this.username = username;
        this.membership = membership;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

//    public void setAvatar(Image avatar) {
//        this.avatar = avatar;
//    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }
}
