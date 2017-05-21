package com.home.client;

import javax.inject.Inject;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.home.client.ApplicationPresenter.MyProxy;

public class ApplicationPresenter extends Presenter<ApplicationView, MyProxy> {
    @ProxyStandard
    public interface MyProxy extends Proxy<ApplicationPresenter> {
    }

    public static final NestedSlot SLOT_MAIN = new NestedSlot();

    @Inject
    ApplicationPresenter(
            EventBus eventBus,
            ApplicationView view,
            MyProxy proxy) {
        super(eventBus, view, proxy, RevealType.Root);
    }
}
