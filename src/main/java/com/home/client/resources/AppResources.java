package com.home.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface AppResources extends ClientBundle {
    AppResources INSTANCE = GWT.create(AppResources.class);

    @ClientBundle.Source("css/login-page-style.css")
    LoginPageStyle loginPageStyle();

    @ClientBundle.Source("css/app-style.css")
    AppStyle appStyle();

}
