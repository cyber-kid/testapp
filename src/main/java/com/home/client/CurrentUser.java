package com.home.client;

import javax.inject.Singleton;

/**
 * Created by cyberkid on 5/21/17.
 */
@Singleton
public class CurrentUser {
    private boolean isLoggedIn;

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
