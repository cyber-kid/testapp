package com.home.client.loginpage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.home.client.ApplicationPresenter;
import com.home.client.api.UserResourseClient;
import com.home.shared.model.CurrentUser;
import com.home.client.places.NameTokens;
import org.fusesource.restygwt.client.Defaults;
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
    private CurrentUser currentUser;

    String name;
    String password;

    UserResourseClient client;
    MethodCallback<CurrentUser> callback = new MethodCallback<CurrentUser>() {
        @Override
        public void onFailure(Method method, Throwable throwable) {
            Window.alert(throwable.getMessage());
        }

        @Override
        public void onSuccess(Method method, CurrentUser user) {
            currentUser.setFirstName(user.getFirstName());
            currentUser.setLastName(user.getLastName());
            currentUser.setPassword(user.getPassword());
            //currentUser.setDob(user.getDob());
            currentUser.setEmail(user.getEmail());

            if (validateCredentials()) {
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
    };

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

        Defaults.setServiceRoot(GWT.getHostPageBaseURL());
        Defaults.setDateFormat(null);
        client = GWT.create(UserResourseClient.class);
    }

    @Override
    protected void onReveal() {
        getView().removeErrorNote();
    }

    @Override
    public void confirm(String username, String password) {
        this.name = username;
        this.password = password;
        client.getUser(username, callback);
    }

    private boolean validateCredentials() {
        return name.equals(currentUser.getEmail()) && password.equals(currentUser.getPassword());
    }

    @Override
    public void signUp() {
        PlaceRequest placeRequest = new PlaceRequest.Builder()
                .nameToken(NameTokens.REGISTER)
                .build();
        placeManager.revealPlace(placeRequest);
    }
}