package com.home.client.loginpage;

import com.gwtplatform.mvp.client.UiHandlers;

/**
 * Created by cyberkid on 5/21/17.
 */
public interface LoginUiHandlers extends UiHandlers {
    public void confirm(String username, String password);
}
