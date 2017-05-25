package com.home.client.loginpage;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.home.client.ApplicationPresenter;
import com.home.client.CurrentUser;
import com.home.client.places.NameTokens;

import javax.inject.Inject;

public class LoginPresenter extends Presenter<LoginView, LoginPresenter.MyProxy>
        implements LoginUiHandlers {

    @ProxyStandard
    @NameToken(NameTokens.LOGIN)
    @NoGatekeeper
    public interface MyProxy extends ProxyPlace<LoginPresenter> {
    }

    // Credentials are stored here for demo purpose only.
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin123";

    private PlaceManager placeManager;
    private CurrentUser currentUser;

    @Inject
    LoginPresenter(
            EventBus eventBus,
            LoginView view,
            MyProxy proxy,
            CurrentUser currentUser,
            PlaceManager placeManager) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN);

        this.currentUser = currentUser;
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

    @Override
    public void confirm(String username, String password) {
        if (validateCredentials(username, password)) {
            currentUser.setLoggedIn();

            PlaceRequest placeRequest = new PlaceRequest.Builder()
                    .nameToken(NameTokens.HOME)
                    .build();
            placeManager.revealPlace(placeRequest);
        } else {
            getView().addErrorNote();
            getView().clearFields();
        }
    }

    private boolean validateCredentials(String username, String password) {
        return username.equals(USERNAME) && password.equals(PASSWORD);
    }

    @Override
    public void signUp() {
        PlaceRequest placeRequest = new PlaceRequest.Builder()
                .nameToken(NameTokens.REGISTER)
                .build();
        placeManager.revealPlace(placeRequest);
    }
}