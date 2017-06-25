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
import com.home.client.utils.ClientFactory;
import com.home.shared.model.AppUser;
import com.home.client.places.NameTokens;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import javax.inject.Inject;

public class LoginPresenter extends Presenter<LoginView, LoginPresenter.MyProxy>
        implements LoginUiHandlers {

    @ProxyStandard
    @NameToken(NameTokens.LOGIN)
    @NoGatekeeper
    public interface MyProxy extends ProxyPlace<LoginPresenter> {
    }

    private PlaceManager placeManager;
    private AppUser appUser;
    private ClientFactory clientFactory;

    private String name;
    private String password;

    private MethodCallback<AppUser> callback = new MethodCallback<AppUser>() {
        @Override
        public void onFailure(Method method, Throwable throwable) {}

        @Override
        public void onSuccess(Method method, AppUser user) {
            if(user != null) {
                appUser.setFirstName(user.getFirstName());
                appUser.setLastName(user.getLastName());
                appUser.setPassword(user.getPassword());
                //appUser.setDob(user.getDob());
                appUser.setEmail(user.getEmail());

                if (validateCredentials()) {
                    appUser.setLoggedIn();

                    PlaceRequest placeRequest = new PlaceRequest.Builder()
                            .nameToken(NameTokens.HOME)
                            .build();
                    placeManager.revealPlace(placeRequest);
                } else {
                    showErrorNote();
                }
            } else {
                showErrorNote();
            }
        }
    };

    @Inject
    LoginPresenter(
            EventBus eventBus,
            LoginView view,
            MyProxy proxy,
            AppUser appUser,
            PlaceManager placeManager,
            ClientFactory clientFactory) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN);

        this.appUser = appUser;
        this.placeManager = placeManager;
        this.clientFactory = clientFactory;

        getView().setUiHandlers(this);
    }

    @Override
    protected void onReveal() {
        getView().removeErrorNote();
    }

    @Override
    public void confirm(String username, String password) {
        this.name = username;
        this.password = password;
        clientFactory.getUserResourceClient().getUser(username, callback);
    }

    @Override
    public void signUp() {
        PlaceRequest placeRequest = new PlaceRequest.Builder()
                .nameToken(NameTokens.REGISTER)
                .build();
        placeManager.revealPlace(placeRequest);
    }

    private boolean validateCredentials() {
        return name.equals(appUser.getEmail()) && password.equals(appUser.getPassword());
    }

    private void showErrorNote() {
        getView().addErrorNote();
        getView().clearFields();
    }
}