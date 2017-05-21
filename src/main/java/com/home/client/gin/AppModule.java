package com.home.client.gin;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.home.client.ApplicationPresenter;
import com.home.client.ApplicationView;
import com.home.client.ApplicationViewImpl;
import com.home.client.CurrentUser;
import com.home.client.homepage.HomePresenter;
import com.home.client.homepage.HomeView;
import com.home.client.homepage.HomeViewImpl;
import com.home.client.loginpage.LoginPresenter;
import com.home.client.loginpage.LoginView;
import com.home.client.loginpage.LoginViewImpl;

import javax.inject.Singleton;

/**
 * Created by cyberkid on 5/21/17.
 */
public class AppModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(ApplicationPresenter.class, ApplicationView.class, ApplicationViewImpl.class,
                ApplicationPresenter.MyProxy.class);
        bindPresenter(HomePresenter.class, HomeView.class, HomeViewImpl.class, HomePresenter.MyProxy.class);
        bindPresenter(LoginPresenter.class, LoginView.class, LoginViewImpl.class, LoginPresenter.MyProxy.class);
        bind(CurrentUser.class).in(Singleton.class);
    }
}