package com.home.client.gin;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.home.client.ApplicationPresenter;
import com.home.client.ApplicationView;
import com.home.client.ApplicationViewImpl;
import com.home.shared.model.CurrentUser;
import com.home.client.homepage.HomePresenter;
import com.home.client.homepage.HomeView;
import com.home.client.homepage.HomeViewImpl;
import com.home.client.loginpage.LoginPresenter;
import com.home.client.loginpage.LoginView;
import com.home.client.loginpage.LoginViewImpl;
import com.home.client.registrationpage.RegistrationPresenter;
import com.home.client.registrationpage.RegistrationView;
import com.home.client.registrationpage.RegistrationViewImpl;

import javax.inject.Singleton;


public class AppModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(ApplicationPresenter.class, ApplicationView.class, ApplicationViewImpl.class,
                ApplicationPresenter.MyProxy.class);
        bindPresenter(HomePresenter.class, HomeView.class, HomeViewImpl.class, HomePresenter.MyProxy.class);
        bindPresenter(LoginPresenter.class, LoginView.class, LoginViewImpl.class, LoginPresenter.MyProxy.class);
        bindPresenter(RegistrationPresenter.class, RegistrationView.class, RegistrationViewImpl.class, RegistrationPresenter.MyProxy.class);
        bind(CurrentUser.class).in(Singleton.class);
    }
}