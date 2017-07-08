package com.nux.dhoan9.firstmvvm.model;

/**
 * Author: hoang
 * Created by: ModelGenerator on 03/07/2017
 */
public class Client {
    private String environment;
    private String paypalSdkVersion;
    private String platform;
    private String productName;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getPaypalSdkVersion() {
        return paypalSdkVersion;
    }

    public void setPaypalSdkVersion(String paypalSdkVersion) {
        this.paypalSdkVersion = paypalSdkVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}