package com.home.server.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.server.utils.AppProperties;
import com.home.shared.model.AppUser;
import com.home.shared.model.KeyValue;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class FlatFileDAO implements DataAccessObject {
    private Properties props = AppProperties.getProps();
    private Logger LOG = Logger.getLogger(FlatFileDAO.class);
    private Path folder = Paths.get(props.getProperty("users.folder", "users"));

    @Override
    public AppUser getUser(String name) {
        ObjectMapper mapper = new ObjectMapper();
        AppUser user = new AppUser();

        LOG.info("Search user: " + name);

        String fileName = name + ".txt";
        Path file = Paths.get(folder.toString(), fileName);

        try {
            user = mapper.readValue(file.toFile(), AppUser.class);
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public AppUser addUser(AppUser user) {
        ObjectMapper mapper = new ObjectMapper();

        if(!Files.exists(folder)) {
            try {
                Files.createDirectories(folder);
                System.out.println(folder.toAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        String fileName = user.getEmail() + ".txt";
        Path file = Paths.get(folder.toString(), fileName);

        try {
            mapper.writeValue(file.toFile(), user);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return user;
    }

    @Override
    public KeyValue testUser(String name) {
        KeyValue result = new KeyValue();

        String fileName = name + ".txt";
        Path file = Paths.get(folder.toString(), fileName);

        if(Files.exists(file)) {
            result.setKey("user");
            result.setValue(name);
        }

        return result;
    }
}
