package com.home.client.registrationpage;

import com.google.gwt.user.client.ui.Button;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.View;
import com.home.client.widgets.LookUp;
import com.home.client.widgets.TextBoxWithValidation;

public interface RegistrationView extends View, HasUiHandlers<RegistrationUiHandlers> {
    TextBoxWithValidation getFirstName();
    Button getSubmit();
    TextBoxWithValidation getLastName();
    TextBoxWithValidation getEmail();
    TextBoxWithValidation getPassword();
    TextBoxWithValidation getPasswordConfirmation();
    LookUp<Integer> getDay();
    LookUp<Integer> getMonth();
    LookUp<Integer> getYear();
}
