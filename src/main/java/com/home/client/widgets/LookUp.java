package com.home.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.*;
import com.google.common.base.Strings;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.home.client.resources.AppResources;
import com.home.client.resources.ErrorNotePopUpStyle;
import com.home.client.resources.LookUpStyle;
import com.home.client.utils.KeyCodesHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LookUp<T> extends Composite implements HasText,
        HasWidgets,
        SelectionHandler<String>,
        MouseOverHandler,
        HasValueChangeHandlers<T> {
    @UiField
    public TextBox input;
    @UiField
    public Label label;
    @UiField
    public Image checkFlag;

    @UiTemplate("LookUpHorizontal.ui.xml")
    interface UiBinderHorizontal extends UiBinder<Widget, LookUp> {}
    private static UiBinderHorizontal horizontalUiBinder = GWT.create(UiBinderHorizontal.class);

    @UiTemplate("LookUpVertical.ui.xml")
    interface UiBinderVertical extends UiBinder<Widget, LookUp> {}
    private static UiBinderVertical verticalUiBinder = GWT.create(UiBinderVertical.class);

    private PopupPanel dropDown = new PopupPanel();
    private FlowPanel panel = new FlowPanel();

    private final LookUpStyle STYLE = AppResources.INSTANCE.lookUpStyle();
    private static final AppResources BUNDLE = AppResources.INSTANCE;
    private final ErrorNotePopUpStyle ERROR_STYLE = AppResources.INSTANCE.errorNotePopUpStyle();

    private T model;
    private boolean isMandatory = false;
    private boolean isValid = false;
    private List<LookUpItem<T>> items = new ArrayList<>();
    private LookUpItem<T> selectedItem;
    private int focusedItemIndex = 0;

    @UiConstructor
    public LookUp(boolean vertical) {
        if (vertical) {
            initWidget(verticalUiBinder.createAndBindUi(this));
        } else {
            initWidget(horizontalUiBinder.createAndBindUi(this));
        }
    }

    @Override
    protected void onLoad() {
        if(isMandatory) {
            label.setStyleName(ERROR_STYLE.mandatoryField(), true);
        }
        initiateDropDown();
        positionDropDown();
    }

    @UiHandler("input")
    public void onClick (ClickEvent clickEvent) {
        if(!dropDown.isShowing()) {
            showDropDown();
        }
    }

    @UiHandler("input")
    public void onTextBoxFocus(FocusEvent event) {
        if(!dropDown.isShowing()) {
            showDropDown();
        }
    }

    @UiHandler("input")
    public void onBlur(BlurEvent blurEvent) {
        setText(input.getText());
    }

    @UiHandler("input")
    public void onKeyUp(KeyUpEvent event) {
        int keyCode = event.getNativeEvent().getKeyCode();

        if (keyCode == KeyCodes.KEY_ENTER) {
            selectItem();
            hideDropDown();
        }

        if (KeyCodesHelper.isLetterKey(keyCode) || KeyCodesHelper.isDigitKey(keyCode) || keyCode == KeyCodes.KEY_BACKSPACE) {
            showDropDown();
        }
    }

    @UiHandler("input")
    public void onArrowsKeyDown (KeyDownEvent keyDownEvent) {
        int keyCode = keyDownEvent.getNativeEvent().getKeyCode();

        if (keyDownEvent.isUpArrow()) {
            keyDownEvent.preventDefault();
            focusPreviousItem();
        }
        
        if(keyDownEvent.isDownArrow()) {
            if(!dropDown.isShowing()) {
                showDropDown();
            } else {
                focusNextItem();
            }
        }

        if (keyCode == KeyCodes.KEY_TAB) {
            hideDropDown();
        }
    }

    @Override
    public void onSelection(SelectionEvent<String> selectionEvent) {
        selectItem();
        hideDropDown();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler valueChangeHandler) {
        return addHandler(valueChangeHandler, ValueChangeEvent.getType());
    }

    @Override
    public void onMouseOver(MouseOverEvent mouseOverEvent) {
        Object eventSource = mouseOverEvent.getSource();
        if (eventSource instanceof LookUpItem) {
            focusItem((LookUpItem<T>) eventSource);
        }
    }

    @Override
    public String getText() {
        return input.getText();
    }

    @Override
    public void setText(String s) {
        input.setText(s);
        validate(s);
        setIcon();
        if(!isValid) {
            showErrorNote();
        }
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

    private void unFocusItem() {
        removeFocusStyle();
    }

    private void focusFirstItem() {
        focusedItemIndex = 0;
        addFocusStyle();
    }

    private void focusItem (LookUpItem<T> item) {
        unFocusItem();
        item.setStyleName(STYLE.focusedItem(), true);
        focusedItemIndex = panel.getWidgetIndex(item);
    }

    private void focusNextItem () {
        unFocusItem();
        if (focusedItemIndex == panel.getWidgetCount() - 1) {
            focusFirstItem();
        } else {
            focusedItemIndex++;
            addFocusStyle();
        }
    }

    private void focusPreviousItem () {
        unFocusItem();
        if (focusedItemIndex == 0) {
            focusedItemIndex = panel.getWidgetCount();
        }
        focusedItemIndex--;
        addFocusStyle();
    }

    private void hideDropDown () {
        unFocusItem();
        dropDown.hide();
    }

    private void showDropDown() {
        unFocusItem();
        filterDropDown(input.getText());
        focusFirstItem();
        dropDown.show();
    }

    private void validate(String str) {
        isValid = !str.isEmpty();
    }

    private void setIcon () {
        if (isValid) {
            checkFlag.setResource(BUNDLE.successIcon());
        } else {
            checkFlag.setResource(BUNDLE.failureIcon());
        }
        checkFlag.setVisible(true);
    }

    private void showErrorNote() {
        PopupPanel note = new PopupPanel();
        FlowPanel container = new FlowPanel();
        SimplePanel arrow = new SimplePanel();

        arrow.setStyleName(ERROR_STYLE.arrow(), true);
        container.setStyleName(ERROR_STYLE.container(), true);

        container.add(arrow);
        container.add(new Label("This field is mandatory and can not be empty."));
        note.add(container);

        note.setAutoHideEnabled(true);
        note.show();

        int noteTop = input.getAbsoluteTop() + (input.getOffsetHeight()/2) - note.getOffsetHeight()/2;
        int arrowTop = note.getOffsetHeight()/2 - arrow.getOffsetHeight()/2;
        int arrowLeft = 0 - arrow.getOffsetWidth();

        arrow.getElement().getStyle().setPropertyPx("top", arrowTop);
        arrow.getElement().getStyle().setPropertyPx("left", arrowLeft);
        note.setPopupPosition(checkFlag.getAbsoluteLeft() + checkFlag.getWidth() + 10, noteTop);
    }

    private void positionDropDown() {
        dropDown.setPopupPosition(input.getAbsoluteLeft(), input.getAbsoluteTop() + input.getOffsetHeight());
        dropDown.getElement().getStyle().setWidth(input.getOffsetWidth(), Style.Unit.PX);
    }

    private void initiateDropDown() {
        dropDown.setStyleName(STYLE.dropDown(), true);
        dropDown.setAutoHideEnabled(true);
        dropDown.add(panel);
    }

    private void filterDropDown(final String searchStr) {
        panel.clear();
        if (!Strings.isNullOrEmpty(searchStr)) {
            items.stream().filter(i -> i.getText().startsWith(searchStr)).forEach(panel::add);
        } else {
            items.forEach(panel::add);
        }
    }

    private void removeFocusStyle() {
        panel.getWidget(focusedItemIndex).setStyleName(STYLE.focusedItem(), false);
    }

    private void addFocusStyle() {
        panel.getWidget(focusedItemIndex).setStyleName(STYLE.focusedItem(), true);
    }

    public String getLabelText() {
        return label.getText();
    }

    public void setLabelText(String lookUpLabelText) {
        label.setText(lookUpLabelText);
    }

    public T getModel() {
        return model;
    }

    private void setModel(T model) {
        this.model = model;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public boolean isValid() {
        return isValid;
    }

    private void setSelectedItem(LookUpItem<T> item) {
        selectedItem = item;
    }

    private void selectItem() {
        Widget item = panel.getWidget(focusedItemIndex);
        if (item instanceof LookUpItem) {
            selectedItem = (LookUpItem<T>)item;
            setModel(selectedItem.getModel());
            setText(selectedItem.getText());
            ValueChangeEvent.fire(this, getModel());
        }
    }

    public void setTabIndex(int index) {
        input.setTabIndex(index);
    }

}
