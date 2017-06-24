package com.home.server.utils;

import java.util.Properties;
import java.util.ResourceBundle;

public class AppProperties {
    private Properties props = new Properties();
    private ResourceBundle rb = ResourceBundle.getBundle("config");

    public Properties getProps() {
        rb.keySet().stream().forEach(k -> props.put(k, rb.getString(k)));
        return props;
    }


}
