package pl.edu.agh.groupcalendar.restservices.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.groupcalendar.dto.Event;
import pl.edu.agh.groupcalendar.ejbs.interfaces.IEventBean;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Bean serving event related REST services.
 *
 * @author Bartosz
 *         Created on 2015-05-30.
 */
@Path("/event")
@Stateless
public class EventService {

    /** Logger. */
    private static final Logger LOGGER = LogManager.getLogger(GroupService.class);

    /** Return JSON when received invalid json. */
    private static final String WRONG_JSON =
            "{\"error_no\":\"-6\", \"error_desc\":\"Received json is not valid in this method.\"}";

    /** Return JSON for ERROR_CODE {@link IEventBean#GROUP_DOES_NOT_EXISTS_ERROR_CODE}. */
    private static final String GROUP_DOES_NOT_EXISTS_JSON =
            "{\"error_no\":\"-1\", \"error_desc\":\"Group with this name does not exists.\"}";

    /** Return JSON for ERROR_CODE {@link IEventBean#NO_RIGHTS_ERROR_CODE NO_RIGHTS_ERROR_CODE}. */
    private static final String NO_RIGHTS_JSON =
            "{\"error_no\":\"-2\", \"error_desc\":\"No right to modify this event.\"}";

    /** Return JSON for ERROR_CODE {@link IEventBean#EVENT_DOES_NOT_EXISTS_ERROR_CODE EVENT_DOES_NOT_EXISTS_ERROR_CODE}. */
    private static final String EVENT_NOT_EXIST_JSON =
            "{\"error_no\":\"-3\", \"error_desc\":\"Event with this name does not exists.\"}";

    /** Object capable of serialization to and from json. */
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /** Bean responsible for group services. */
    @EJB
    private IEventBean eventBean;

    /**
     * Creates group in a system
     * @param eventString event JSON string
     * @param groupName name of group to which event belongs
     * @return {@link Response.Status#OK OK} when group created,
     *      {@link Response.Status#CONFLICT CONFLICT} with appropriate message when creating did not succeed
     *      {@link Response.Status#BAD_REQUEST BAD_REQUEST} when group JSON is not valid
     */
    @POST
    @Path("/create/{groupname}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Context HttpHeaders httpHeaders, final String eventString,
                           @PathParam("groupname") final String groupName) {
        Event event;
        try {
            event = gson.fromJson(eventString, Event.class);
        } catch (JsonSyntaxException e) {
            LOGGER.error("create", e);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(WRONG_JSON).build();
        }

        String result = eventBean.create(event, groupName, httpHeaders.getHeaderString(LoginService.SESSION_KEY));

        if(IEventBean.GROUP_DOES_NOT_EXISTS_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
                    .entity(GROUP_DOES_NOT_EXISTS_JSON).build();
        } else if(IEventBean.NO_RIGHTS_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
                    .entity(NO_RIGHTS_JSON).build();
        }

        return Response.ok().build();
    }

    /**
     * Creates group in a system
     * @param eventString event JSON string
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
                         @DefaultValue("false") @QueryParam("del") final boolean delete, final String eventString) {
        Event event;
        try {
            event = gson.fromJson(eventString, Event.class);
        } catch (JsonSyntaxException e) {
            LOGGER.error("create", e);
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(WRONG_JSON).build();
        }

        String result = eventBean.modify(event, httpHeaders.getHeaderString(LoginService.SESSION_KEY), delete);

        if(IEventBean.EVENT_DOES_NOT_EXISTS_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
                    .entity(EVENT_NOT_EXIST_JSON).build();
        } else if(IEventBean.NO_RIGHTS_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
                    .entity(NO_RIGHTS_JSON).build();
        }

        return Response.ok().build();
    }

    /**
     * Returns group to whom user belongs
     * @return {@link Response.Status#OK OK} when group created,
     *      {@link Response.Status#CONFLICT CONFLICT} with appropriate message when adding did not succeed
     */
    @GET
    @Path("/groupList/{groupname}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response groupsList(@PathParam("groupname") final String groupName) {
        List<Event> result = eventBean.eventsList(groupName);

        return Response.status(Response.Status.OK).entity(result).build();
    }
}
