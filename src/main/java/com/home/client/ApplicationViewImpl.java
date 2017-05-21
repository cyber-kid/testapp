package com.home.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.gwtplatform.mvp.client.ViewImpl;

/**
 * Created by cyberkid on 5/21/17.
 */
public class ApplicationViewImpl extends ViewImpl implements ApplicationView {
    private final SimplePanel main;

    ApplicationViewImpl() {
        main = new SimplePanel();

        initWidget(main);
        bindSlot(ApplicationPresenter.SLOT_MAIN, main);
    }
}
