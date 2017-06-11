package com.home.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.*;
import com.google.common.base.Strings;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import com.home.client.resources.AppResources;
import com.home.client.resources.ErrorNotePopUpStyle;
import com.home.client.resources.LookUpStyle;
import com.home.client.utils.KeyCodesHelper;

import java.util.*;

public class LookUp<T> extends Composite implements HasText,
        HasWidgets,
        SelectionHandler<LookUpItem<T>>,
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

    private boolean isMandatory = false;
    private boolean isValid = false;
    private Set<LookUpItem<T>> items = new HashSet<>();
    private List<String> errorMessages = new ArrayList<>();
    private LookUpItem<T> selectedItem;
    private int focusedItemIndex = -1;
    private PopupPanel errorNote = new PopupPanel();

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
        validateAndSelect();
    }

    @UiHandler("input")
    public void onKeyUp(KeyUpEvent event) {
        int keyCode = event.getNativeEvent().getKeyCode();

        if (keyCode == KeyCodes.KEY_ENTER) {
            setTextFromFocusedItem();
            validateAndSelect();
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
            validateAndSelect();
        }
    }

    @Override
    public void onSelection(SelectionEvent<LookUpItem<T>> selectionEvent) {
        focusItem(selectionEvent.getSelectedItem());
        setTextFromFocusedItem();
        validateAndSelect();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler valueChangeHandler) {
        return addHandler(valueChangeHandler, ValueChangeEvent.getType());
    }

    @Override
    public void onMouseOver(MouseOverEvent mouseOverEvent) {
            focusItem(mouseOverEvent.getSource());
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
        widget = widget.equals(selectedItem) ? selectedItem : widget;
        items.add(widget);
        panel.add(widget);

        if(selectedItem != null) {
            focusItem(selectedItem);
            validate();
        }
    }

    @Override
    public void clear() {
        items.clear();
        panel.clear();
    }

    @Override
    public Iterator<Widget> iterator() {
        return panel.iterator();
    }

    @Override
    public boolean remove(Widget widget) {
        throw new IllegalArgumentException("Only LookUpItem is allowed");
    }

    public boolean remove(LookUpItem<T> widget) {
        return items.remove(widget) && panel.remove(widget);
    }

    private void unFocusItem() {
        if (focusedItemIndex >= 0 && focusedItemIndex < panel.getWidgetCount()) {
            removeFocusStyle();
        }
    }

    private void focusFirstItem() {
        if(panel.getWidgetCount() > 0) {
            focusedItemIndex = 0;
            addFocusStyle();
        } else {
            focusedItemIndex = -1;
        }
    }

    private void focusItem (Object item) {
        unFocusItem();
        if(item instanceof LookUpItem) {
            LookUpItem<T> lookUpItem = (LookUpItem<T>)item;
            lookUpItem.setStyleName(STYLE.focusedItem(), true);
            focusedItemIndex = panel.getWidgetIndex(lookUpItem);
        }
    }

    private void setTextFromFocusedItem() {
        if (focusedItemIndex >= 0 && focusedItemIndex < panel.getWidgetCount()) {
            Object widget = panel.getWidget(focusedItemIndex);
            if(widget instanceof LookUpItem) {
                input.setText(((LookUpItem<T>)widget).getText());
            }
        }
    }

    private void focusNextItem () {
        unFocusItem();
        if(panel.getWidgetCount() > 0) {
            if (focusedItemIndex == panel.getWidgetCount() - 1) {
                focusFirstItem();
            } else {
                focusedItemIndex++;
                addFocusStyle();
            }
        } else {
            focusedItemIndex = -1;
        }
    }

    private void focusPreviousItem () {
        unFocusItem();
        if(panel.getWidgetCount() > 0) {
            if (focusedItemIndex == 0) {
                focusedItemIndex = panel.getWidgetCount();
            }
            focusedItemIndex--;
            addFocusStyle();
        } else {
            focusedItemIndex = -1;
        }
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

    private void validate() {
        errorMessages.clear();
        if(!input.getText().isEmpty()) {
            if(focusedItemIndex < 0) {
                errorMessages.add("This value is not valid for this field.");
                isValid = false;
            } else {
                isValid = true;
            }
        } else {
            if(isMandatory) {
                errorMessages.add("This field is mandatory and can not be empty.");
                isValid = false;
            } else {
                isValid = true;
            }
        }
        setIcon();
        if(!isValid) {
            showErrorNote();
        } else {
            errorNote.hide();
        }
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
        errorNote.clear();
        FlowPanel container = new FlowPanel();
        SimplePanel arrow = new SimplePanel();

        arrow.setStyleName(ERROR_STYLE.arrow(), true);
        container.setStyleName(ERROR_STYLE.container(), true);

        container.add(arrow);
        errorMessages.forEach(s -> container.add(new Label(s)));
        errorNote.add(container);

        errorNote.setAutoHideEnabled(true);
        errorNote.show();

        int noteTop = input.getAbsoluteTop() + (input.getOffsetHeight()/2) - errorNote.getOffsetHeight()/2;
        int arrowTop = errorNote.getOffsetHeight()/2 - arrow.getOffsetHeight()/2;
        int arrowLeft = 0 - arrow.getOffsetWidth();

        arrow.getElement().getStyle().setPropertyPx("top", arrowTop);
        arrow.getElement().getStyle().setPropertyPx("left", arrowLeft);
        errorNote.setPopupPosition(checkFlag.getAbsoluteLeft() + checkFlag.getWidth() + 10, noteTop);
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

    private void selectItem() {
        Widget item = panel.getWidget(focusedItemIndex);
        if (item instanceof LookUpItem) {
            setSelectedItem((LookUpItem<T>)item);
            ValueChangeEvent.fire(this, getModel());
        }
    }

    private void clearSelectedItem() {
        setSelectedItem(null);
    }

    private void validateAndSelect() {
        validate();
        if(isValid) {
            selectItem();
        } else {
            clearSelectedItem();
        }
        hideDropDown();
    }

    private void removeFocusStyle() {panel.getWidget(focusedItemIndex).setStyleName(STYLE.focusedItem(), false);}

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
        return selectedItem.getModel();
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

    public void setTabIndex(int index) {
        input.setTabIndex(index);
    }
}
