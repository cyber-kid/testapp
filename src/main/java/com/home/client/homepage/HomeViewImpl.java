package com.home.client.homepage;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

import javax.inject.Inject;

public class HomeViewImpl extends ViewImpl implements HomeView {
    interface Binder extends UiBinder<Widget, HomeViewImpl> {}

    @Inject
    HomeViewImpl(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
}
