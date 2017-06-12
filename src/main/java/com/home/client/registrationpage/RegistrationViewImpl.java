package com.home.client.registrationpage;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.home.client.widgets.LookUp;
import com.home.client.widgets.TextBoxWithValidation;

import javax.inject.Inject;

public class RegistrationViewImpl extends ViewWithUiHandlers<RegistrationUiHandlers> implements RegistrationView {
    interface Binder extends UiBinder<Widget, RegistrationViewImpl> {};

    @UiField
    TextBoxWithValidation firstName;
    @UiField
    TextBoxWithValidation lastName;
    @UiField
    TextBoxWithValidation email;
    @UiField
    TextBoxWithValidation password;
    @UiField
    TextBoxWithValidation passwordConfirmation;
    @UiField
    LookUp<Integer> day;
    @UiField
    LookUp<Integer> month;
    @UiField
    LookUp<Integer> year;
    @UiField
    Button submit;
    /*@UiField
    Button cancel;*/

    @Inject
    RegistrationViewImpl(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler({"firstName", "lastName", "email", "passwordConfirmation"})
    public void onValueChange(ValueChangeEvent<String> changeEvent) {
        getUiHandlers().onValueChange();
    }

    @UiHandler("day")
    public void onDayValueChange(ValueChangeEvent<Integer> changeEvent) {
        getUiHandlers().onDayLookUpValueChange(changeEvent.getValue());
    }

    @UiHandler("month")
    public void onMonthValueChange(ValueChangeEvent<Integer> changeEvent) {
        getUiHandlers().onMonthLookUpValueChange(changeEvent.getValue());
    }

    @UiHandler("year")
    public void onYearValueChange(ValueChangeEvent<Integer> changeEvent) {
        getUiHandlers().onValueChange();
    }

    @UiHandler("password")
    public void onPasswordChange(ValueChangeEvent<String> changeEvent) {
        getUiHandlers().onPasswordValueChange();
    }

    @UiHandler("submit")
    public void onSubmit(ClickEvent clickEvent) {
        getUiHandlers().onSubmit();
    }

    @UiHandler("cancel")
    public void onCancel(ClickEvent clickEvent) {
        getUiHandlers().onCancel();
    }

    @UiHandler("clear")
    public void onClear(ClickEvent clickEvent) {
        getUiHandlers().onClear();
    }

    public TextBoxWithValidation getFirstName() {
        return firstName;
    }

    public Button getSubmit() {
        return submit;
    }

    public TextBoxWithValidation getLastName() {
        return lastName;
    }

    public TextBoxWithValidation getEmail() {
        return email;
    }

    public TextBoxWithValidation getPassword() {
        return password;
    }

    public TextBoxWithValidation getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public LookUp<Integer> getDay() {
        return day;
    }

    public LookUp<Integer> getMonth() {
        return month;
    }

    public LookUp<Integer> getYear() {
        return year;
    }
}
