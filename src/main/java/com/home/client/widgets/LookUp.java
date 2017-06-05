package com.home.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.common.base.Strings;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import com.home.client.resources.AppResources;
import com.home.client.resources.LookUpStyle;
import com.home.client.utils.KeyCodesHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LookUp<T> extends Composite implements HasText,
        HasWidgets,
        SelectionHandler<String>,
        MouseOverHandler {
    @UiTemplate("LookUpHorizontal.ui.xml")
    interface UiBinderHorizontal extends UiBinder<Widget, LookUp> {}
    private static UiBinderHorizontal horizontalUiBinder = GWT.create(UiBinderHorizontal.class);

    @UiTemplate("LookUpVertical.ui.xml")
    interface UiBinderVertical extends UiBinder<Widget, LookUp> {}
    private static UiBinderVertical verticalUiBinder = GWT.create(UiBinderVertical.class);

    private PopupPanel dropDown = new PopupPanel();
    private FlowPanel panel = new FlowPanel();
    private List<LookUpItem<T>> items = new ArrayList<>();
    private int focusedItemIndex = -1;
    private final LookUpStyle STYLE = AppResources.INSTANCE.lookUpStyle();
    private T model;

    @UiField
    public TextBox input;
    @UiField
    public Label lookUpLabel;

    @UiConstructor
    public LookUp(boolean vertical) {
        if (vertical) {
            initWidget(verticalUiBinder.createAndBindUi(this));
        } else {
            initWidget(horizontalUiBinder.createAndBindUi(this));
        }
        initiateDropDown();
    }

    @UiHandler("input")
    public void onClick (ClickEvent clickEvent) {
        positionPopUp();
        showDropDown();
    }

    @UiHandler("input")
    public void onTextBoxFocus(FocusEvent event) {
        positionPopUp();
        showDropDown();
    }

    private void positionPopUp() {
        dropDown.setPopupPosition(input.getAbsoluteLeft(), input.getAbsoluteTop() + input.getOffsetHeight());
        dropDown.getElement().getStyle().setWidth(input.getOffsetWidth(), Style.Unit.PX);
    }

    @UiHandler("input")
    public void onKeyUp(KeyUpEvent event) {
        int keyCode = event.getNativeEvent().getKeyCode();

        if (keyCode == KeyCodes.KEY_ENTER) {
            setText(getTextFromFocusedItem());
            hideDropDown();
        }
        if (KeyCodesHelper.isLetterKey(keyCode) || keyCode == KeyCodes.KEY_BACKSPACE) {
            Object eventSource = event.getSource();
            if (eventSource instanceof TextBox) {
                TextBox i = (TextBox) eventSource;
                filterDropDown(i.getText());
            }
        }
    }

    @UiHandler("input")
    public void onArrowsKeyDown (KeyDownEvent keyDownEvent) {
        if (keyDownEvent.isUpArrow()) {
            keyDownEvent.preventDefault();
            focusPreviousItem();
        }
        if(keyDownEvent.isDownArrow()) {
            showDropDown();
            focusNextItem();
        }
    }

    private void initiateDropDown() {
        dropDown.setStyleName(STYLE.dropDown(), true);
        dropDown.setAutoHideEnabled(true);
        dropDown.add(panel);
    }

    private void filterDropDown(final String searchStr) {
        panel.clear();
        /*if (searchStr != null || searchStr.length() != 0) {*/
        if (!Strings.isNullOrEmpty(searchStr)) {
            items.stream().filter(i -> i.getText().startsWith(searchStr)).forEach(panel::add);
        } else {
            items.stream().forEach(panel::add);
        }
        showDropDown();
    }

    @Override
    public void onSelection(SelectionEvent<String> selectionEvent) {
        setText(selectionEvent.getSelectedItem());
        hideDropDown();
    }

    @Override
    public void onMouseOver(MouseOverEvent mouseOverEvent) {
        Object eventSource = mouseOverEvent.getSource();
        if (eventSource instanceof LookUpItem) {
            focusItem((LookUpItem<T>)eventSource);
        }
    }

    private void unFocusItem() {
        if (focusedItemIndex >= 0) {
            panel.getWidget(focusedItemIndex).setStyleName(STYLE.focusedItem(), false);
        }
    }

    private void focusItem (LookUpItem<T> item) {
        unFocusItem();
        item.setStyleName(STYLE.focusedItem(), true);
        focusedItemIndex = panel.getWidgetIndex(item);
    }

    private void focusNextItem () {
        unFocusItem();
        if (focusedItemIndex == panel.getWidgetCount() - 1) {
            focusedItemIndex = -1;
        }

        panel.getWidget(++focusedItemIndex).setStyleName(STYLE.focusedItem(), true);
    }

    private void focusPreviousItem () {
        unFocusItem();
        if (focusedItemIndex == 0 || focusedItemIndex == -1) {
            focusedItemIndex = panel.getWidgetCount();
        }
        panel.getWidget(--focusedItemIndex).setStyleName(STYLE.focusedItem(), true);
    }

    private void hideDropDown () {
        dropDown.hide();
        unFocusItem();
    }

    private String getTextFromFocusedItem() {
        return ((LookUpItem<T>)panel.getWidget(focusedItemIndex)).getText();
    }

    private void showDropDown() {
        if (!dropDown.isShowing()) {
            focusedItemIndex = -1;
            dropDown.show();
        }
    }

    @Override
    public String getText() {
        return input.getText();
    }

    @Override
    public void setText(String s) {
        input.setText(s);
    }

    @Override
    public void add(Widget widget) {
        throw new IllegalArgumentException("Only LookUpItem is allowed");
    }

    public void add(LookUpItem<T> widget) {
        widget.addSelectionHandler(this);
        widget.addMouseOverHandler(this);
        items.add(widget);
        panel.add(widget);
    }

    @Override
    public void clear() {
        items.clear();
        panel.clear();
    }

    @Override
    public Iterator<Widget> iterator() {
        return null;
    }

    @Override
    public boolean remove(Widget widget) {
        throw new IllegalArgumentException("Only LookUpItem is allowed");
    }

    public boolean remove(LookUpItem<T> widget) {
        return items.remove(widget) && panel.remove(widget);
    }

    public String getLookUpLabelText() {
        return lookUpLabel.getText();
    }

    public void setLookUpLabelText(String lookUpLabelText) {
        lookUpLabel.setText(lookUpLabelText);
    }

    public T getModel() {
        getModelFromSelectedItem();
        return model;
    }

    private void getModelFromSelectedItem() {
        Widget item = panel.getWidget(focusedItemIndex);
        if (item instanceof LookUpItem) {
            model = ((LookUpItem<T>)item).getModel();
        }
    }
}
