package com.home.server.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.server.dao.DataAccessObject;
import com.home.server.dao.FlatFileDAO;
import com.home.shared.model.AppUser;
import com.home.shared.model.TestItem;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.file.*;

@Path("/")
public class UserResource {
    private DataAccessObject dao = new FlatFileDAO();

    @GET
    @Path("/user")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public AppUser getUser(@QueryParam("name") String name) {
        return dao.getUser(name);
    }

    @POST
    @Path("/newUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AppUser addUser(AppUser user) {
        return dao.addUser(user);
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
