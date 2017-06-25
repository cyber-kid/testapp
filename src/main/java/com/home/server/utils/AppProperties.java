package com.home.server.utils;

import java.util.Properties;
import java.util.ResourceBundle;

public class AppProperties {
    private static Properties props = new Properties();
    private static ResourceBundle rb = ResourceBundle.getBundle("config");

    public static Properties getProps() {
        rb.keySet().stream().forEach(k -> props.put(k, rb.getString(k)));
        return props;
    }


}
