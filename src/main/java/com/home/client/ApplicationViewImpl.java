package com.home.client;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

import javax.inject.Inject;

/**
 * Created by cyberkid on 5/21/17.
 */
public class ApplicationViewImpl extends ViewImpl implements ApplicationView {
    interface Binder extends UiBinder<Widget, ApplicationViewImpl> {
    }
    @UiField
    SimplePanel main;

    @Inject
    ApplicationViewImpl(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        bindSlot(ApplicationPresenter.SLOT_MAIN, main);
    }
}
