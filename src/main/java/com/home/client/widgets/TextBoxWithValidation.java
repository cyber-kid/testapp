package com.home.client.widgets;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.home.client.resources.AppResources;
import com.home.client.resources.TextBoxValidationStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class TextBoxWithValidation extends Composite {
    @UiField
    Label label;

    @UiField
    TextBox input;

    @UiField
    Image checkFlag;

    private static final AppResources BUNDLE = AppResources.INSTANCE;
    private final TextBoxValidationStyle STYLE = AppResources.INSTANCE.textBoxValidation();
    private boolean isValid = false;
    private boolean isMandatory = false;
    private Map<String, RegExp> checks = new HashMap<>();
    private List<Label> errorLabels = new ArrayList<>();

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
    public void onLoad() {
        if(isMandatory) {
            label.setStyleName(STYLE.mandatoryField(), true);
        }
    }

    @UiHandler("input")
    public void onBlur(BlurEvent blurEvent) {
        validate(input.getText());
        setIcon();
        if(!isValid) {
            showNoteWithErrors();
        }
    }

    public void setLabelForText(String label) {
        this.label.setText(label);
    }

    public boolean isValid() {
        return isValid;
    }

    private void setValid(boolean valid) {
        isValid = valid;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public Map<String, RegExp> getChecks() {
        return checks;
    }

    public void setChecks(Map<String, RegExp> checks) {
        this.checks = checks;
    }

    public void addCheck(String message, RegExp pattern) {
        checks.put(message, pattern);
    }

    public void setIcon () {
        if (isValid) {
            checkFlag.setResource(BUNDLE.successIcon());
        } else {
            checkFlag.setResource(BUNDLE.failureIcon());
        }
        checkFlag.setVisible(true);
    }

    private void validate(String str) {
        errorLabels.clear();
        if(isMandatory() && Strings.isNullOrEmpty(str)) {
            errorLabels.add(new Label("This field is mandatory and can not be empty."));
        } else if (!Strings.isNullOrEmpty(str)) {
            BiConsumer<String, RegExp> action = (s, p) -> {
                if(p.exec(str) == null) {
                    errorLabels.add(new Label(s));
                }
            };
            checks.forEach(action);
        }

        setValid(errorLabels.isEmpty());
    }

    private void showNoteWithErrors() {
        PopupPanel note = new PopupPanel();
        FlowPanel container = new FlowPanel();
        SimplePanel arrow = new SimplePanel();

        arrow.setStyleName(STYLE.arrow(), true);
        container.setStyleName(STYLE.container(), true);

        container.add(arrow);
        errorLabels.forEach(container::add);
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
}
