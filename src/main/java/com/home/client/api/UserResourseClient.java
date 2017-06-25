package com.home.client.api;

import com.home.shared.model.AppUser;
import com.home.shared.model.KeyValue;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface UserResourseClient extends RestService {
    @POST
    @Path("/api/newUser")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void addUser(AppUser user, MethodCallback<AppUser> callback);

    @GET
    @Path("/api/test")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public void getTest(@QueryParam("name") String name, MethodCallback<KeyValue> callback);

    @GET
    @Path("/api/user")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public void getUser(@QueryParam("name") String name, MethodCallback<AppUser> callback);
}
