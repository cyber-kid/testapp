package com.home.client;

import javax.inject.Singleton;

@Singleton
public class CurrentUser {
    private boolean isLoggedIn;

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn() {
        isLoggedIn = true;
    }
}
