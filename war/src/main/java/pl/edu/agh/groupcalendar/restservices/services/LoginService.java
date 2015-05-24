package pl.edu.agh.groupcalendar.restservices.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.edu.agh.groupcalendar.dto.User;
import pl.edu.agh.groupcalendar.ejbs.interfaces.IAuthBean;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Bartosz
 *         Created on 2015-05-21.
 */
@Path("/auth")
@Stateless
public class LoginService {

    public static final String SERVICE_KEY = "service_key";

    public static final String AUTH_TOKEN = "auth_token";

    @EJB
    private IAuthBean authBean;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/status")
    public Response getStatus() {
        return Response.ok("{\"status\":\"Service is running...\"}").build();
    }

    @POST
    @Path("/login/{credentials}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpHeaders httpHeaders, @PathParam("credentials") String credentials) {
        return null;
    }

    @POST
    @Path("/logout")
    public Response logout(@Context HttpHeaders httpHeaders) {
        return null;
    }

    @PUT
    @Path("/createUser")
    @Consumes(MediaType.APPLICATION_JSON)
    public void createUser(String user) {
        Gson gson = new Gson();
        User user1 = gson.fromJson(user, User.class);
        authBean.insertUser(user1);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getUsers")
    public Response getUsers() {
        List<User> users = authBean.getUsers();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return Response.ok(gson.toJson(users)).build();
    }


}
