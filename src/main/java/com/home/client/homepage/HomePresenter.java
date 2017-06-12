package com.home.client.homepage;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.home.client.ApplicationPresenter;
import com.home.client.places.NameTokens;
import com.home.shared.CurrentUser;

import javax.inject.Inject;
import java.util.Date;

public class HomePresenter extends Presenter<HomeView, HomePresenter.MyProxy> {
    @ProxyStandard
    @NameToken(NameTokens.HOME)
    public interface MyProxy extends ProxyPlace<HomePresenter> {
    }

    private CurrentUser user;

    @Inject
    HomePresenter(
            EventBus eventBus,
            HomeView view,
            CurrentUser user,
            MyProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN);
        this.user = user;
        showUserDetails();
    }

    private void showUserDetails() {
        getView().getFirstName().setText(user.getFirstName());
        getView().getLastName().setText(user.getLastName());
        getView().getEmail().setText(user.getEmail());
        getView().getDob().setText(user.getDob().toString());
    }
}
