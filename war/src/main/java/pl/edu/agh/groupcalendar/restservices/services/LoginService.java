package pl.edu.agh.groupcalendar.restservices.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    /** Logger. */
    private static final Logger LOGGER = LogManager.getLogger(LoginService.class);

    /** Service key header tag. */
    public static final String SERVICE_KEY = "service_key";

    /** Auth token header tag. */
    public static final String SESSION_KEY = "session_key";

    /** Return JSON for ERROR_CODE {@link IAuthBean#NO_SUCH_USER_ERROR_CODE NO_SUCH_USER_ERROR_CODE}. */
    private static final String NO_USER_JSON =
            "{\"error_no\":\"-1\", \"error_desc\":\"User does not exists.\"}";

    /** Return JSON for ERROR_CODE {@link IAuthBean#WRONG_CREDENTIALS_ERROR_CODE WRONG_CREDENTIALS_ERROR_CODE}. */
    private static final String WRONG_CREDENTIALS_JSON =
            "{\"error_no\":\"-2\", \"error_desc\":\"Wrong credentials.\"}";

    /** Return JSON when user session does not exists. */
    private static final String SESSION_NOT_EXISTS_JSON =
            "{\"error_no\":\"-3\", \"error_desc\":\"Session does not exists.\"}";

    /** Return JSON for ERROR_CODE {@link IAuthBean#USERNAME_EXISTS_ERROR_CODE USERNAME_EXISTS_ERROR_CODE}. */
    private static final String USERNAME_EXISTS_JSON =
            "{\"error_no\":\"-4\", \"error_desc\":\"Username already exists.\"}";

    /** Return JSON for ERROR_CODE {@link IAuthBean#EMAIL_EXISTS_ERROR_CODE EMAIL_EXISTS_ERROR_CODE}. */
    private static final String EMAIL_EXISTS_JSON =
            "{\"error_no\":\"-5\", \"error_desc\":\"Email already exists.\"}";

    /** Return JSON when received invalid json. */
    private static final String WRONG_JSON =
            "{\"error_no\":\"-6\", \"error_desc\":\"Received json is not valid in this method.\"}";

    /** Object capable of serialization to and from json. */
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /** Bean responsible for authorization services. */
    @EJB
    private IAuthBean authBean;

    /**
     * Returns service status.
     * @return {@link Response.Status#OK OK} if service is running
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/status")
    public Response getStatus() {
        return Response.ok("{\"status\":\"Service is running...\"}").build();
    }

    /**
     * Logs an user to system.
     * @param httpHeaders header with {@link #SERVICE_KEY inside}
     * @param credentials user credentials
     * @return {@link Response.Status#OK OK} with {@link #SESSION_KEY} of created session,
     *      {@link Response.Status#CONFLICT CONFLICT} with appropriate message when login did not succeed
     */
    @POST
    @Path("/login/{credentials}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpHeaders httpHeaders, @PathParam("credentials") String credentials) {
        String sessionKey = authBean.login(credentials);

        if (IAuthBean.NO_SUCH_USER_ERROR_CODE.equals(sessionKey)) {
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
                    .entity(NO_USER_JSON).build();
        } else if (IAuthBean.WRONG_CREDENTIALS_ERROR_CODE.equals(sessionKey)) {
            return Response.status(Response.Status.UNAUTHORIZED).type(MediaType.APPLICATION_JSON)
                    .entity(WRONG_CREDENTIALS_JSON).build();
        }

        return Response.ok("{\"" + SESSION_KEY + "\":\"" + sessionKey + "\"}").build();
    }

    /**
     * Logs user out of a system
     * @param httpHeaders header with {@link #SERVICE_KEY inside} and {@link #SESSION_KEY}
     * @param username username
     * @return {@link Response.Status#OK OK} when user logged out,
     *      {@link Response.Status#CONFLICT CONFLICT} with appropriate message when login out did not succeed
     */
    @POST
    @Path("/logout/{username}")
    public Response logout(@Context HttpHeaders httpHeaders, @PathParam("username") String username) {
        boolean result = authBean.logout(httpHeaders.getHeaderString(LoginService.SESSION_KEY), username);

        if (!result) {
            return Response.status(Response.Status.CONFLICT).entity(SESSION_NOT_EXISTS_JSON).build();
        }

        return Response.ok().build();
    }

    /**
     * Register user in a system
     * @param userString user JSON string
     * @return {@link Response.Status#OK OK} when user registered,
     *      {@link Response.Status#CONFLICT CONFLICT} with appropriate message when registering did not succeed
     *      {@link Response.Status#BAD_REQUEST BAD_REQUEST} when user JSON is not valid
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(final String userString) {
        User user;
        try {
            user = gson.fromJson(userString, User.class);
        } catch (JsonSyntaxException e) {
            LOGGER.error("register", e);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON)
                    .entity(WRONG_JSON).build();
        }

        String result = authBean.register(user);
        if (IAuthBean.USERNAME_EXISTS_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
                    .entity(USERNAME_EXISTS_JSON).build();
        } else if (IAuthBean.EMAIL_EXISTS_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
                    .entity(EMAIL_EXISTS_JSON).build();
        }

        return Response.ok().build();
    }

    /**
     * Deletes user from a system
     * @param userString user JSON string
     * @return {@link Response.Status#OK OK} when user deleted,
     *      {@link Response.Status#CONFLICT CONFLICT} with appropriate message when registering did not succeed,
     *      {@link Response.Status#BAD_REQUEST BAD_REQUEST} when user JSON is not valid
     */
    @POST
    @Path("/delete/{credentials}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUser(final String userString, @PathParam("credentials") String credentials) {
        User user;
        try {
            user = gson.fromJson(userString, User.class);
        } catch (JsonSyntaxException e) {
            LOGGER.error("register", e);
            return Response.status(Response.Status.BAD_REQUEST).entity(WRONG_JSON).build();
        }
        String result = authBean.deleteUser(user, credentials);

        if(IAuthBean.NO_SUCH_USER_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
                    .entity(NO_USER_JSON).build();
        } else if(IAuthBean.WRONG_CREDENTIALS_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
                    .entity(WRONG_CREDENTIALS_JSON).build();
        }

        return Response.ok().build();
    }
}
