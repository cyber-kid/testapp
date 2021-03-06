package com.home.client.widgets;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import com.home.client.resources.AppResources;
import com.home.client.resources.ErrorNotePopUpStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class TextBoxWithValidation extends Composite implements HasValueChangeHandlers<String>,
        HasText,
        HasBlurHandlers {
    @UiField
    Label label;
    @UiField
    TextBox input;
    @UiField
    Image checkFlag;
    @UiField
    PasswordTextBox password;

    private static final AppResources BUNDLE = AppResources.INSTANCE;
    private final ErrorNotePopUpStyle ERROR_STYLE = AppResources.INSTANCE.errorNotePopUpStyle();

    private boolean isValid = false;
    private boolean isMandatory = false;
    private boolean isPassword = false;
    private boolean delayedValidation = false;

    private Map<String, RegExp> checks = new HashMap<>();
    private List<String> errorMessages = new ArrayList<>();
    private PopupPanel errorNote = new PopupPanel();

    @UiTemplate("TextBoxWithValidationHorizontal.ui.xml")
    interface UiBinderHorizontal extends UiBinder<Widget, TextBoxWithValidation> {}
    private static UiBinderHorizontal horizontalUiBinder = GWT.create(UiBinderHorizontal.class);

    @UiTemplate("TextBoxWithValidationVertical.ui.xml")
    interface UiBinderVertical extends UiBinder<Widget, TextBoxWithValidation> {}
    private static UiBinderVertical verticalUiBinder = GWT.create(UiBinderVertical.class);

    @UiConstructor
    public TextBoxWithValidation (boolean vertical) {
        if (vertical) {
            initWidget(verticalUiBinder.createAndBindUi(this));
        } else {
            initWidget(horizontalUiBinder.createAndBindUi(this));
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> valueChangeHandler) {
        return addHandler(valueChangeHandler, ValueChangeEvent.getType());
    }

    @Override
    public HandlerRegistration addBlurHandler(BlurHandler blurHandler) {
        return addDomHandler(blurHandler, BlurEvent.getType());
    }

    @Override
    public void onLoad() {
        if(isMandatory) {
            label.setStyleName(ERROR_STYLE.mandatoryField(), true);
        }
        if(isPassword) {
            password.setVisible(true);
        } else {
            input.setVisible(true);
        }
    }

    @Override
    public void setText(String s) {
        if(!isPassword) {
            input.setText(s);
        }
    }

    @Override
    public String getText() {
        if(isPassword) {
            return password.getText();
        } else {
            return input.getText();
        }
    }

    @UiHandler("input")
    public void onInputBlur(BlurEvent blurEvent) {
        if(!delayedValidation) {
            validateFiled();
        } else {
            BlurEvent.fireNativeEvent(blurEvent.getNativeEvent(), this);
        }
    }

    @UiHandler("password")
    public void onPasswordBlur(BlurEvent blurEvent) {
        if(!delayedValidation) {
            validateFiled();
        } else {
            BlurEvent.fireNativeEvent(blurEvent.getNativeEvent(), this);
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

    private void validateInput(String str) {
        errorMessages.clear();
        if(isMandatory && Strings.isNullOrEmpty(str)) {
            errorMessages.add("This field is mandatory and can not be empty.");
        } else if (!Strings.isNullOrEmpty(str)) {
            BiConsumer<String, RegExp> action = (s, p) -> {
                if(p.exec(str) == null) {
                    errorMessages.add(s);
                }
            };
            checks.forEach(action);
        }

        setValid(errorMessages.isEmpty());
    }

    public void validateFiled() {
        String text = isPassword ? password.getText() : input.getText();
        validateInput(text);
        setIcon();
        if(!isValid) {
            showNoteWithErrors();
        }
        ValueChangeEvent.fire(this, input.getText());
    }

    private void showNoteWithErrors() {
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

        int noteTop;
        if(isPassword) {
            noteTop = password.getAbsoluteTop() + (password.getOffsetHeight()/2) - errorNote.getOffsetHeight()/2;
        } else {
            noteTop = input.getAbsoluteTop() + (input.getOffsetHeight() / 2) - errorNote.getOffsetHeight() / 2;
        }
        int arrowTop = errorNote.getOffsetHeight()/2 - arrow.getOffsetHeight()/2;
        int arrowLeft = 0 - arrow.getOffsetWidth();

        arrow.getElement().getStyle().setPropertyPx("top", arrowTop);
        arrow.getElement().getStyle().setPropertyPx("left", arrowLeft);
        errorNote.setPopupPosition(checkFlag.getAbsoluteLeft() + checkFlag.getWidth() + 10, noteTop);
    }

    public void setLabelText(String label) {
        this.label.setText(label);
    }

    public boolean isValid() {
        return isValid;
    }

    private void setValid(boolean valid) {
        isValid = valid;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public void setChecks(Map<String, RegExp> checks) {
        this.checks = checks;
    }

    public void addCheck(String message, RegExp pattern) {
        checks.put(message, pattern);
    }

    public void setPassword(boolean password) {
        isPassword = password;
    }

    public boolean isDelayedValidation() {
        return delayedValidation;
    }

    public void setDelayedValidation(boolean delayedValidation) {
        this.delayedValidation = delayedValidation;
    }

    public void setTabIndex(int index) {
        if(isPassword) {
            password.setTabIndex(index);
        } else {
            input.setTabIndex(index);
        }
    }

    public void reset() {
            password.setText("");
            input.setText("");
            errorNote.clear();
            checkFlag.setVisible(false);
    }
}
