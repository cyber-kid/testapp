package com.home.client.loginpage;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import javax.inject.Inject;

public class LoginViewImpl extends ViewWithUiHandlers<LoginUiHandlers> implements LoginView {
    interface Binder extends UiBinder<Widget, LoginViewImpl> {
    }

    @UiField
    Button confirm;
    @UiField
    TextBox username;
    @UiField
    PasswordTextBox password;
    @UiField
    Label errorNote;

    @Inject
    LoginViewImpl(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("confirm")
    void onConfirm(ClickEvent event) {
        getUiHandlers().confirm(username.getText(), password.getText());
    }

    @UiHandler("signUp")
    void onSignUp(ClickEvent event) {
        getUiHandlers().signUp();
    }

    @Override
    public void addErrorNote() {
        errorNote.setVisible(true);
    }

    @Override
    public void clearFields() {
        username.setText("");
        password.setText("");
    }
}

