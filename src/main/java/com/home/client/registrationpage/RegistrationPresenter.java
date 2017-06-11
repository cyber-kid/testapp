package com.home.client.registrationpage;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.home.client.ApplicationPresenter;
import com.home.client.places.NameTokens;
import com.home.client.widgets.LookUpItem;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class RegistrationPresenter extends Presenter<RegistrationView, RegistrationPresenter.MyProxy> implements RegistrationUiHandlers {
    @ProxyStandard
    @NameToken(NameTokens.REGISTER)
    @NoGatekeeper
    public interface MyProxy extends ProxyPlace<RegistrationPresenter> {}

    private Map<Integer, String> months = new HashMap<>();

    private int dayOfBirth;
    private int monthOfBirth;
    private int yearOfBirth;

    @Inject
    RegistrationPresenter(
            EventBus eventBus,
            RegistrationView view,
            MyProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN);
        getView().setUiHandlers(this);
        buildDaysLookUp();
        initMonthsMap();
    }

    @Override
    public void onValueChange() {
        validate();
    }

    @Override
    public void onDayLookUpValueChange(int day) {
        validate();
        dayOfBirth = day;
        buildMonthsLookUp(dayOfBirth);
    }

    @Override
    public void onMonthLookUpValueChange(int month) {
        validate();
        monthOfBirth = month;
        buildYearLookUp(dayOfBirth, monthOfBirth);
    }

    private void validate() {
        if(
            getView().getFirstName().isValid() && getView().getLastName().isValid() &&
            getView().getEmail().isValid() && getView().getPassword().isValid() &&
            getView().getPasswordConfirmation().isValid() && getView().getDay().isValid() &&
            getView().getMonth().isValid() && getView().getYear().isValid()
        ) {
            enableSubmitButton();
        } else {
            disableSubmitButton();
        }
    }

    private void disableSubmitButton() {
        getView().getSubmit().setEnabled(false);
    }

    private void enableSubmitButton() {
        getView().getSubmit().setEnabled(true);
    }

    private void buildDaysLookUp() {
        for(int i = 1; i <= 31; i++) {
            LookUpItem<Integer> item = new LookUpItem<>();
            item.setText(String.valueOf(i));
            item.setModel(i);
            getView().getDay().add(item);
        }
    }

    private Predicate<Map.Entry<Integer, String>> with31DayFilter() {
        return (month) -> {
            switch (month.getKey()) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    return true;
                default:
                    return false;
            }
        };
    }

    private Predicate<Map.Entry<Integer, String>> with30DayFilter() {
        return (month) -> {
            switch (month.getKey()) {
                case 1:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                    return true;
                default:
                    return false;
            }
        };
    }

    private void buildMonthsLookUp(int day) {
        getView().getMonth().clear();

        Consumer<Map.Entry<Integer, String>> action = (entry) -> {
            LookUpItem<Integer> item = new LookUpItem<>();
            item.setModel(entry.getKey());
            item.setText(entry.getValue());
            getView().getMonth().add(item);
        };

        switch (day) {
            case 31:
                months.entrySet().stream().filter(with31DayFilter()).forEach(action);
                break;
            case 30:
                months.entrySet().stream().filter(with30DayFilter()).forEach(action);
                break;
            default:
                months.entrySet().forEach(action);
        }
    }

    private void initMonthsMap() {
        months.put(1, "January");
        months.put(2, "February");
        months.put(3, "March");
        months.put(4, "April");
        months.put(5, "May");
        months.put(6, "June");
        months.put(7, "July");
        months.put(8, "August");
        months.put(9, "September");
        months.put(10, "October");
        months.put(11, "November");
        months.put(12, "December");
    }

    private IntPredicate findLeapYear() {
        return (year) -> {
            if (year%4 != 0) {
                return false;
            } else if (year%100 != 0) {
                return true;
            } else if (year%400 == 0) {
                return false;
            } else {
                return true;
            }
        };
    }

    private void buildYearLookUp(int day, int month) {
        getView().getYear().clear();

        IntConsumer action = (year) -> {
            LookUpItem<Integer> item = new LookUpItem<>();
            item.setModel(year);
            item.setText(String.valueOf(year));
            getView().getYear().add(item);
        };

        if(day == 29 && month ==2) {
            IntStream.rangeClosed(1950, 2017).filter(findLeapYear()).forEach(action);
        } else {
            IntStream.rangeClosed(1950, 2017).forEach(action);
        }
    }
}
