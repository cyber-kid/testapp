package com.home.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;

public class LookUpItem<T> extends Composite implements HasText,
        HasSelectionHandlers<String>,
        HasMouseOverHandlers {
    interface Binder extends UiBinder<Widget, LookUpItem> {}

    private static Binder uiBinder = GWT.create(Binder.class);

    private T model;

    @UiField
    public Label item;

    public LookUpItem() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public String getText() {
        return item.getText();
    }

    @Override
    public void setText(String s) {
        item.setText(s);
    }

    @UiHandler("item")
    public void onItemClick(ClickEvent event) {
        SelectionEvent.fire(this, item.getText());
    }

    @UiHandler("main")
    public void onEnterKeyPress(KeyDownEvent event) {
        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
            SelectionEvent.fire(this, item.getText());
        }
    }

    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<String> selectionHandler) {
        return addHandler(selectionHandler, SelectionEvent.getType());
    }

    @Override
    public HandlerRegistration addMouseOverHandler(MouseOverHandler mouseOverHandler) {
        return addDomHandler(mouseOverHandler, MouseOverEvent.getType());
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }
}
