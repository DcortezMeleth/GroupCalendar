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

/**
 * Bean serving login REST services.
 *
 * @author Bartosz
 *         Created on 2015-05-21.
 */
@Path("/auth")
@Stateless
public class LoginService {

    /** Service key header tag. */
    public static final String SERVICE_KEY = "service_key";

    /** Auth token header tag. */
    public static final String SESSION_KEY = "session_key";

    private static final String NO_USER_JSON = "{\"error_no\":\"-1\", \"error_desc\":\"User does not exists.\"}";

    private static final String WRONG_PASSWORD_JSON = "{\"error_no\":\"-2\", \"error_desc\":\"Wrong password.\"}";

    private static final String SESSION_NOT_EXISTS_JSON = "{\"error_no\":\"-3\", \"error_desc\":\"Session does not exists.\"}";

    private static final String USERNAME_EXISTS_JSON = "{\"error_no\":\"-4\", \"error_desc\":\"Username already exists.\"}";

    private static final String EMAIL_EXISTS_JSON = "{\"error_no\":\"-5\", \"error_desc\":\"Email already exists.\"}";

    /** Object capable of serialization to and from json. */
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

        if (IAuthBean.NO_SUCH_USER_ERROR_CODE.equals(sessionKey)) {
            return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(NO_USER_JSON).build();
        } else if (IAuthBean.WRONG_PASSWORD_ERROR_CODE.equals(sessionKey)) {
            return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(WRONG_PASSWORD_JSON).build();
        }

        return Response.ok("{\"" + SESSION_KEY + "\":\"" + sessionKey + "\"}").build();
    }

    @POST
    @Path("/logout/{username}")
    public Response logout(@Context HttpHeaders httpHeaders, @PathParam("username") String username) {
        boolean result = authBean.logout(httpHeaders.getHeaderString(LoginService.SESSION_KEY), username);

        if (!result) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(SESSION_NOT_EXISTS_JSON).build();
        }

        return Response.ok().build();
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(final String userString) {
        User user = gson.fromJson(userString, User.class);
        String result = authBean.register(user);

        if (IAuthBean.USERNAME_EXISTS_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(USERNAME_EXISTS_JSON).build();
        } else if (IAuthBean.EMAIL_EXISTS_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON).entity(EMAIL_EXISTS_JSON).build();
        }

        return Response.ok().build();
    }
}
