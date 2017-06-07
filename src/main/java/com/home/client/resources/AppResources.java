package com.home.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface AppResources extends ClientBundle {
    AppResources INSTANCE = GWT.create(AppResources.class);

    @Source("css/login-page-style.css")
    LoginPageStyle loginPageStyle();

    @Source("css/app-style.css")
    AppStyle appStyle();

    @Source("css/look-up.css")
    LookUpStyle lookUpStyle();

    @Source("images/check-passed.png")
    ImageResource successIcon();

    @Source("images/check-failed.png")
    ImageResource failureIcon();

    @Source("css/text-box-validation.css")
    TextBoxValidationStyle textBoxValidation();

    @Source("css/error-note-popup.css")
    ErrorNotePopUpStyle errorNotePopUpStyle();
}
