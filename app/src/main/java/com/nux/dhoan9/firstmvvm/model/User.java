package com.nux.dhoan9.firstmvvm.model;

import com.google.gson.annotations.SerializedName;
/**
 * Created by hoang on 27/03/2017.
 */

public class User {
    private int id;
    @SerializedName("first_name")
    private String firstName;
    private String avatar;
    private String phone;
    private String address;
    private String email;
    private String role;
    @SerializedName("last_name")
    private String lastName;
    private String provider;
    private String uid;

    public User(int id, String firstName, String avatar, String phone,
                String address, String email, String role, String lastName,
                String provider, String uid) {
        this.id = id;
        this.firstName = firstName;
        this.avatar = avatar;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.role = role;
        this.lastName = lastName;
        this.provider = provider;
        this.uid = uid;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProvider() {
        return provider;
    }

    public String getUid() {
        return uid;
    }
}
