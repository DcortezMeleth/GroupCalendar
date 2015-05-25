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

    private static final String NO_USER_JSON = "{\"error_no\":\"-1\", \"error_desc\":\"User does not exists.\"}";

    private static final String WRONG_PASSWORD_JSON = "{\"error_no\":\"-2\", \"error_desc\":\"Wrong password.\"}";

    private static final String SESSION_NOT_EXISTS_JSON = "{\"error_no\":\"-3\", \"error_desc\":\"Session does not exists.\"}";

    /** Obiekt do serializacji. */
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

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
        String sessionKey = authBean.login(credentials);

        if(IAuthBean.NO_SUCH_USER_ERROR_CODE.equals(sessionKey)) {
            return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(NO_USER_JSON).build();
        } else if(IAuthBean.WRONG_PASSWORD_ERROR_CODE.equals(sessionKey)) {
            return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(WRONG_PASSWORD_JSON).build();
        }

        return Response.ok("{\"session_key\":\"" + sessionKey + "\"}").build();
    }

    @POST
    @Path("/logout/{username}")
    public Response logout(@Context HttpHeaders httpHeaders, @PathParam("username") String username) {
        boolean result = authBean.logout(httpHeaders.getHeaderString(LoginService.AUTH_TOKEN), username);

        if(!result) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(SESSION_NOT_EXISTS_JSON).build();
        }

        return Response.ok().build();
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

        return Response.ok(gson.toJson(users)).build();
    }

}
