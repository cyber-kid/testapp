package com.home.client.registrationpage;

import com.google.gwt.regexp.shared.RegExp;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;
import com.home.client.ApplicationPresenter;
import com.home.client.places.NameTokens;
import com.home.client.widgets.LookUpItem;
import com.home.shared.CurrentUser;

import javax.inject.Inject;
import java.util.Date;
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


    private CurrentUser user;
    private PlaceManager placeManager;

    private Map<Integer, String> months = new HashMap<>();

    private int dayOfBirth;
    private int monthOfBirth;
    private int yearOfBirth;

    @Inject
    RegistrationPresenter(
            EventBus eventBus,
            RegistrationView view,
            CurrentUser currentUser,
            PlaceManager placeManager,
            MyProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN);
        user = currentUser;
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
        initMonthsMap();
        buildDaysLookUp();
        buildMonthsLookUp(0);
        buildYearLookUp(0,0);
        addNamesCheck();
        addPasswordCheck();
        addEmailCheck();
    }

    @Override
    public void onValueChange() {
        validate();
    }

    @Override
    public void onEmailFieldBlur() {
        getView().getEmail().addCheck("Such user already exists", RegExp.compile("[^amyrgorod@gmail.com]"));
        getView().getEmail().validateFiled();
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

    @Override
    public void onPasswordValueChange() {
        validate();
        addPasswordConfirmationCheck();
    }

    @Override
    public void onSubmit() {
        setUserDetails();
        redirectToHomePage();
    }

    @Override
    public void onCancel() {
        resetFields();
        cancelRegistration();
    }

    @Override
    public void onClear() {
        resetFields();
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
                case 0:
                case 2:
                case 4:
                case 6:
                case 7:
                case 9:
                case 11:
                    return true;
                default:
                    return false;
            }
        };
    }

    private Predicate<Map.Entry<Integer, String>> with30DayFilter() {
        return (month) -> {
            switch (month.getKey()) {
                case 0:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
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
                break;
        }
    }

    private void initMonthsMap() {
        months.put(0, "January");
        months.put(1, "February");
        months.put(2, "March");
        months.put(3, "April");
        months.put(4, "May");
        months.put(5, "June");
        months.put(6, "July");
        months.put(7, "August");
        months.put(8, "September");
        months.put(9, "October");
        months.put(10, "November");
        months.put(11, "December");
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

        if(day == 29 && month == 1) {
            IntStream.rangeClosed(1950, 2017).filter(findLeapYear()).forEach(action);
        } else {
            IntStream.rangeClosed(1950, 2017).forEach(action);
        }
    }

    private void addNamesCheck() {
        RegExp namesPattern = RegExp.compile("^[A-Za-z\\s]+$");

        getView().getFirstName().addCheck("This field can contain only letters.", namesPattern);
        getView().getLastName().addCheck("This field can contain only letters.", namesPattern);
    }

    private void addPasswordCheck() {
        RegExp digitPattern = RegExp.compile(".*\\d+.*");
        RegExp upperCaseLetterPattern = RegExp.compile(".*[A-Z]+.*");
        RegExp lowerCaseLetterPattern = RegExp.compile(".*[a-z]+.*");
        RegExp specialCharacterPattern = RegExp.compile(".*[$@$!%*#?&]+.*");
        RegExp lengthPattern = RegExp.compile(".{8,}");

        getView().getPassword().addCheck("This field should contain at least one digit.", digitPattern);
        getView().getPassword().addCheck("This field should contain at least one lowercase letter.", lowerCaseLetterPattern);
        getView().getPassword().addCheck("This field should contain at least one uppercase letter.", upperCaseLetterPattern);
        getView().getPassword().addCheck("This field should contain at least one special character.", specialCharacterPattern);
        getView().getPassword().addCheck("This field should contain at least 8 characters.", lengthPattern);
    }

    private void addEmailCheck() {
        RegExp emailPattern = RegExp.compile("^[\\w._%+-]+@[\\w.-]+\\.[A-Za-z]{2,6}$");

        getView().getEmail().addCheck("Not a valid email. Example: john_doe.personal@host.com", emailPattern);
    }

    private void addPasswordConfirmationCheck() {
        boolean isValidPassword = getView().getPassword().isValid();
        RegExp passwordConfirmationPattern = RegExp.compile("");
        if(isValidPassword) {
            passwordConfirmationPattern = RegExp.compile(getView().getPassword().getText());
        }
        getView().getPasswordConfirmation().addCheck("The value does not match previously entered password.", passwordConfirmationPattern);
    }

    private void setUserDetails() {
        user.setFirstName(getView().getFirstName().getText());
        user.setLastName(getView().getLastName().getText());
        user.setEmail(getView().getEmail().getText());
        user.setPassword(getView().getPassword().getText()); //TODO encrypt password

        yearOfBirth = getView().getYear().getModel();
        monthOfBirth = getView().getMonth().getModel();
        dayOfBirth = getView().getDay().getModel();
        Date dob = new Date(yearOfBirth, monthOfBirth, dayOfBirth);
        user.setDob(dob);
        user.setLoggedIn();
    }

    private void redirectToHomePage() {
        PlaceRequest placeRequest = new PlaceRequest.Builder()
                .nameToken(NameTokens.HOME)
                .build();
        placeManager.revealPlace(placeRequest);
    }

    private void resetFields() {
        getView().getFirstName().reset();
        getView().getLastName().reset();
        getView().getEmail().reset();
        getView().getPassword().reset();
        getView().getPasswordConfirmation().reset();
        getView().getDay().reset();
        getView().getMonth().reset();
        getView().getYear().reset();
        getView().getSubmit().setEnabled(false);
    }

    private void cancelRegistration() {
        PlaceRequest placeRequest = new PlaceRequest.Builder()
                .nameToken(NameTokens.LOGIN)
                .build();
        placeManager.revealPlace(placeRequest);
    }
}
