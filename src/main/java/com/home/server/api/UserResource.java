package com.home.server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.shared.model.CurrentUser;
import com.home.shared.model.TestItem;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.file.*;

@Path("/")
public class UserResource {
    @GET
    @Path("/user")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public CurrentUser getUser(@QueryParam("name") String name) {
        ObjectMapper mapper = new ObjectMapper();
        CurrentUser user = new CurrentUser();

        java.nio.file.Path folder = Paths.get("users");

        String fileName = name.split("@")[0] + ".txt";
        java.nio.file.Path file = Paths.get(folder.toString(), fileName);

        try {
            user = mapper.readValue(file.toFile(), CurrentUser.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }

    @POST
    @Path("/newUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CurrentUser addUser(CurrentUser user) {
        ObjectMapper mapper = new ObjectMapper();

        java.nio.file.Path folder = Paths.get("users");
        if(!Files.exists(folder)) {
            try {
                Files.createDirectories(folder);
                System.out.println(folder.toAbsolutePath().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String fileName = user.getEmail().split("@")[0] + ".txt";
        java.nio.file.Path file = Paths.get(folder.toString(), fileName);

        try {
            mapper.writeValue(file.toFile(), user);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error!");
        }
        return user;
    }

    @GET
    @Path("/test")
    @Produces(MediaType.APPLICATION_JSON)
    public TestItem getTest() {
        TestItem item = new TestItem();
        item.setKey("result");
        item.setValue("It works!");

        return item;
    }
}
