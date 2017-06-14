package com.home.client.utils;

import com.google.gwt.resources.client.CssResource;
import com.home.client.resources.AppResources;

import javax.inject.Inject;

public class StyleInjector {
    private final CssResource[] STYLES = new CssResource[] {
            AppResources.INSTANCE.appStyle(),
            AppResources.INSTANCE.loginPageStyle(),
            AppResources.INSTANCE.lookUpStyle(),
            AppResources.INSTANCE.textBoxValidation(),
            AppResources.INSTANCE.errorNotePopUpStyle(),
            AppResources.INSTANCE.registrationStyle()
    };

    @Inject
    void ensureInjected() {
        for(CssResource style : STYLES) {
            style.ensureInjected();
        }
    }
}
