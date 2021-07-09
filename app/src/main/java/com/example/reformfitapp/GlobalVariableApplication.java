package com.example.reformfitapp;

import android.app.Application;


public class GlobalVariableApplication extends Application {

    private Boolean logIn;
    private String clientId;
    private MindbodyClientResponseModel mindbodyClientResponseModel = null;

    private boolean home;

    public GlobalVariableApplication() {
        this.logIn = false;
        mindbodyClientResponseModel = new MindbodyClientResponseModel();

    }

    public Boolean getLogIn() {
        return logIn;
    }

    public void setLogIn(Boolean logInEx) {
        this.logIn = logInEx;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public MindbodyClientResponseModel getMindbodyClientResponseModel() {
        return mindbodyClientResponseModel;
    }

    public void setMindbodyClientResponseModel(MindbodyClientResponseModel mindbodyClientResponseModel) {
        this.mindbodyClientResponseModel = mindbodyClientResponseModel;
    }

    public boolean isHome() {
        return home;
    }

    public void setHome(boolean home) {
        this.home = home;
    }
}
