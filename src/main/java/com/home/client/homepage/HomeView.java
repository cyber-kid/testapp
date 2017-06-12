package com.home.client.homepage;

import com.google.gwt.user.client.ui.Label;
import com.gwtplatform.mvp.client.View;

public interface HomeView extends View {
    Label getFirstName();
    Label getLastName();
    Label getEmail();
    Label getDob();
}
