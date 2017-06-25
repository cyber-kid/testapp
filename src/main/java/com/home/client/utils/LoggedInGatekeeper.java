package com.home.client.utils;

import com.gwtplatform.mvp.client.annotations.DefaultGatekeeper;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;
import com.home.shared.model.AppUser;

import javax.inject.Inject;

@DefaultGatekeeper
public class LoggedInGatekeeper implements Gatekeeper {
    private AppUser appUser;

    @Inject
    public LoggedInGatekeeper(AppUser appUser) {
        this.appUser = appUser;
    }

    @Override
    public boolean canReveal() {
        return appUser.isLoggedIn();
    }
}