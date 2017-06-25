package com.home.client.utils;

import com.google.gwt.core.client.GWT;
import com.home.client.api.UserResourseClient;
import org.fusesource.restygwt.client.Defaults;

public class ClientFactory {
    public UserResourseClient getUserResourceClient() {
        Defaults.setServiceRoot(GWT.getHostPageBaseURL());
        Defaults.setDateFormat(null);

        return GWT.create(UserResourseClient.class);
    }
}
