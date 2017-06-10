package com.home.client.loginpage;

import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.View;

public interface LoginView extends View, HasUiHandlers<LoginUiHandlers> {
    void addErrorNote();
    void removeErrorNote();
    void clearFields();
}
