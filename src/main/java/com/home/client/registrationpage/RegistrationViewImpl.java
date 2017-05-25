package com.home.client.registrationpage;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import javax.inject.Inject;

public class RegistrationViewImpl extends ViewWithUiHandlers<RegistrationUiHandlers> implements RegistrationView {
    interface Binder extends UiBinder<Widget, RegistrationViewImpl> {};

    @Inject
    RegistrationViewImpl(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
}
