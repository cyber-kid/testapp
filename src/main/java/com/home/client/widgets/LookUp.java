package com.home.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import javax.inject.Inject;

public class LookUp extends Composite implements HasHandlers {
    interface Binder extends UiBinder<Widget, LookUp> {}

    private static Binder uiBinder = GWT.create(Binder.class);

    @UiField
    public TextBox input;

    @Inject
    LookUp() {
        initWidget(uiBinder.createAndBindUi(this));
    }
}
