package com.home.server.dao;

import com.home.shared.model.AppUser;
import com.home.shared.model.KeyValue;

import java.io.IOException;

public interface DataAccessObject {
    AppUser getUser(String name);
    AppUser addUser(AppUser user);
    KeyValue testUser(String name);
}
