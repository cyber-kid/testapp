package com.home.client.loginpage;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.home.client.resources.AppResources;
import com.home.client.resources.LoginPageStyle;

import javax.inject.Inject;

/**
 * Created by cyberkid on 5/21/17.
 */
public class LoginViewImpl extends ViewWithUiHandlers<LoginUiHandlers> implements LoginView {
    interface Binder extends UiBinder<Widget, LoginViewImpl> {
    }

    @UiField
    Button confirm;
    @UiField
    TextBox username;
    @UiField
    PasswordTextBox password;

    @Inject
    LoginViewImpl(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("confirm")
    void onConfirm(ClickEvent event) {
        getUiHandlers().confirm(username.getText(), password.getText());
    }
}

