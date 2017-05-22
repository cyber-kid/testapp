package com.home.client.utils;

import com.google.gwt.resources.client.CssResource;
import com.home.client.resources.AppResources;

import javax.inject.Inject;

/**
 * Created by amyrgorod on 22.5.2017 г..
 */
public class StyleInjector {
    private final CssResource[] STYLES = new CssResource[] {
            AppResources.INSTANCE.appStyle(),
            AppResources.INSTANCE.loginPageStyle()
    };

    @Inject
    void ensureInjected() {
        for(CssResource style : STYLES) {
            style.ensureInjected();
        }
    }
}
