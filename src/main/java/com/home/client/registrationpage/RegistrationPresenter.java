package com.home.client.registrationpage;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.home.client.ApplicationPresenter;
import com.home.client.places.NameTokens;

import javax.inject.Inject;

public class RegistrationPresenter extends Presenter<RegistrationView, RegistrationPresenter.MyProxy> {
    @ProxyStandard
    @NameToken(NameTokens.REGISTER)
    @NoGatekeeper
    public interface MyProxy extends ProxyPlace<RegistrationPresenter> {}

    @Inject
    RegistrationPresenter(
            EventBus eventBus,
            RegistrationView view,
            MyProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN);
    }
}
