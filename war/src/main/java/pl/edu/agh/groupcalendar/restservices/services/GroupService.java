package pl.edu.agh.groupcalendar.restservices.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.groupcalendar.dto.Group;
import pl.edu.agh.groupcalendar.ejbs.interfaces.IGroupBean;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Bean serving group connected REST services.
 *
 * @author Bartosz
 *         Created on 2015-05-30.
 */
@Path("/group")
@Stateless
public class GroupService {

    /** Logger. */
    private static final Logger LOGGER = LogManager.getLogger(GroupService.class);

    /** Return JSON when received invalid json. */
    private static final String WRONG_JSON =
            "{\"error_no\":\"-6\", \"error_desc\":\"Received json is not valid in this method.\"}";

    /** Return JSON for ERROR_CODE {@link IGroupBean#GROUP_ALREADY_EXISTS_ERROR_CODE GROUP_ALREADY_EXISTS_ERROR_CODE}. */
    private static final String GROUP_ALREADY_EXISTS_JSON =
            "{\"error_no\":\"-1\", \"error_desc\":\"Group with this name already exists.\"}";

    /** Return JSON for ERROR_CODE {@link IGroupBean#GROUP_DOES_NOT_EXISTS_ERROR_CODE GROUP_DOES_NOT_EXISTS_ERROR_CODE}. */
    private static final String GROUP_NOT_EXIST_JSON =
            "{\"error_no\":\"-2\", \"error_desc\":\"Group with this name does not exists.\"}";

    /** Return JSON for ERROR_CODE {@link IGroupBean#NO_RIGHTS_ERROR_CODE NO_RIGHTS_ERROR_CODE}. */
    private static final String NO_RIGHTS_JSON =
            "{\"error_no\":\"-3\", \"error_desc\":\"No right to modify this group.\"}";

    /** Return JSON for ERROR_CODE {@link IGroupBean#NO_RIGHTS_ERROR_CODE NO_RIGHTS_ERROR_CODE}. */
    private static final String USER_DOES_NOT_EXIST_JSON =
            "{\"error_no\":\"-4\", \"error_desc\":\"No right to modify this group.\"}";

    /** Object capable of serialization to and from json. */
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /** Bean responsible for group services. */
    @EJB
    private IGroupBean groupBean;

    /**
     * Creates group in a system
     * @param groupString group JSON string
     * @return {@link Response.Status#OK OK} when group created,
     *      {@link Response.Status#CONFLICT CONFLICT} with appropriate message when creating did not succeed
     *      {@link Response.Status#BAD_REQUEST BAD_REQUEST} when group JSON is not valid
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Context HttpHeaders httpHeaders, final String groupString) {
        Group group;
        try {
            group = gson.fromJson(groupString, Group.class);
        } catch (JsonSyntaxException e) {
            LOGGER.error("create", e);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(WRONG_JSON).build();
        }

        String result = groupBean.create(group, httpHeaders.getHeaderString(LoginService.SESSION_KEY));

        if(IGroupBean.GROUP_ALREADY_EXISTS_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
                    .entity(GROUP_ALREADY_EXISTS_JSON).build();
        }

        return Response.ok().build();
    }

    /**
     * Creates group in a system
     * @param groupString group JSON string
     * @param delete optional query param, indicates if group should be removed, default is false
     * @return {@link Response.Status#OK OK} when group created,
     *      {@link Response.Status#CONFLICT CONFLICT} with appropriate message when editing did not succeed
     *      {@link Response.Status#BAD_REQUEST BAD_REQUEST} when group JSON is not valid
     */
    @POST
    @Path("/edit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response edit(@Context HttpHeaders httpHeaders,
                         @DefaultValue("false") @QueryParam("del") final boolean delete, final String groupString) {
        Group group;
        try {
            group = gson.fromJson(groupString, Group.class);
        } catch (JsonSyntaxException e) {
            LOGGER.error("create", e);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(WRONG_JSON).build();
        }

        String result = groupBean.modify(group, httpHeaders.getHeaderString(LoginService.SESSION_KEY), delete);

        if(IGroupBean.GROUP_DOES_NOT_EXISTS_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
                    .entity(GROUP_NOT_EXIST_JSON).build();
        } else if(IGroupBean.NO_RIGHTS_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
                    .entity(NO_RIGHTS_JSON).build();
        }

        return Response.ok().build();
    }

    /**
     * Adds user to a group
     * @param groupName name of group to join
     * @param username name of user to add
     * @return {@link Response.Status#OK OK} when group created,
     *      {@link Response.Status#CONFLICT CONFLICT} with appropriate message when adding did not succeed
     */
    @POST
    @Path("/join")
    @Produces(MediaType.APPLICATION_JSON)
    public Response join(@Context HttpHeaders httpHeaders, @QueryParam("groupname") final String groupName,
                         @QueryParam("username") final String username) {
        String result = groupBean.join(groupName, username, httpHeaders.getHeaderString(LoginService.SESSION_KEY));

        if(IGroupBean.GROUP_DOES_NOT_EXISTS_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
                    .entity(GROUP_NOT_EXIST_JSON).build();
        } else if(IGroupBean.NO_RIGHTS_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
                    .entity(NO_RIGHTS_JSON).build();
        } else if(IGroupBean.USER_DOES_NOT_EXISTS_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
                    .entity(USER_DOES_NOT_EXIST_JSON).build();
        }

        return Response.ok().build();
    }

    /**
     * Removes user from a group
     * @param groupName name of group to join
     * @param username name of user to add
     * @return {@link Response.Status#OK OK} when group created,
     *      {@link Response.Status#CONFLICT CONFLICT} with appropriate message when adding did not succeed
     */
    @POST
    @Path("/leave")
    @Produces(MediaType.APPLICATION_JSON)
    public Response leave(@Context HttpHeaders httpHeaders, @QueryParam("groupname") final String groupName,
                         @QueryParam("username") final String username) {
        String result = groupBean.leave(groupName, username, httpHeaders.getHeaderString(LoginService.SESSION_KEY));

        if(IGroupBean.GROUP_DOES_NOT_EXISTS_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
                    .entity(GROUP_NOT_EXIST_JSON).build();
        } else if(IGroupBean.NO_RIGHTS_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
                    .entity(NO_RIGHTS_JSON).build();
        } else if(IGroupBean.USER_DOES_NOT_EXISTS_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
                    .entity(USER_DOES_NOT_EXIST_JSON).build();
        }

        return Response.ok().build();
    }

    /**
     * Returns group to whom user belongs
     * @return {@link Response.Status#OK OK} when group created,
     *      {@link Response.Status#CONFLICT CONFLICT} with appropriate message when adding did not succeed
     */
    @GET
    @Path("/groupList")
    @Produces(MediaType.APPLICATION_JSON)
    public Response groupsList(@Context HttpHeaders httpHeaders) {
        List<Group> result = groupBean.groupsList(httpHeaders.getHeaderString(LoginService.SESSION_KEY));

        return Response.status(Response.Status.OK).entity(result).build();
    }
}
