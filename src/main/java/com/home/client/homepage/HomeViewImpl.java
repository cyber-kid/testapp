package com.home.client.homepage;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

import javax.inject.Inject;

public class HomeViewImpl extends ViewImpl implements HomeView {
    interface Binder extends UiBinder<Widget, HomeViewImpl> {}

    @UiField
    Label firstName;
    @UiField
    Label lastName;
    @UiField
    Label email;
    @UiField
    Label dob;

    @Inject
    HomeViewImpl(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public Label getFirstName() {
        return firstName;
    }

    public Label getLastName() {
        return lastName;
    }

    public Label getEmail() {
        return email;
    }

    public Label getDob() {
        return dob;
    }

}
