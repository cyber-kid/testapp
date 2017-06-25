package com.home.client.registrationpage;

import com.gwtplatform.mvp.client.UiHandlers;

public interface RegistrationUiHandlers extends UiHandlers {
    void onValueChange();
    void onDayLookUpValueChange(int day);
    void onMonthLookUpValueChange(int month);
    void onPasswordValueChange();
    void onSubmit();
    void onCancel();
    void onClear();
    void onEmailFieldBlur();
}
