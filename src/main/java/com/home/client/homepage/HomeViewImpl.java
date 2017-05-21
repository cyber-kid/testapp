package com.home.client.homepage;

import com.google.gwt.user.client.ui.Label;
import com.gwtplatform.mvp.client.ViewImpl;

public class HomeViewImpl extends ViewImpl implements HomeView {
    HomeViewImpl() {
        initWidget(new Label("Hello World!"));
    }
}
