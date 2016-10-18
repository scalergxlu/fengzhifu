package com.scaler.resource;


import com.scaler.util.Base64;
import feng.manager.ServiceManager;
import feng.model.User;
import net.minidev.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/*
* 用户通用rest
* */
@Path("/user")
public class UserResource {
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject login(@FormParam("mobile") String username, @FormParam  ("pwd") String password) {

//        //解码
        String uname = Base64.getFromBASE64(username);
        String pwd = Base64.getFromBASE64(password);

        return ServiceManager.iUserService.login(uname,pwd);
    }

    @GET
    @Path("/userlist")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUserinfo(@QueryParam("username") String username, @QueryParam("password") String password) {

//        //解码
//        String uname = Base64.getFromBASE64(username);
//        String pwd = Base64.getFromBASE64(password);

        return ServiceManager.iUserService.getUserList();
    }

}
