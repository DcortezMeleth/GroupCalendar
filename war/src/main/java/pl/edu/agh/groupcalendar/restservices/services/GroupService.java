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
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

    /** Return JSON for ERROR_CODE {@link IGroupBean#GROUP_ALREDY_EXISTS_ERROR_CODE GROUP_ALREDY_EXISTS_ERROR_CODE}. */
    private static final String GROUP_ALREADY_EXISTS_JSON =
            "{\"error_no\":\"-1\", \"error_desc\":\"Group with this name already exists.\"}";

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
     *      {@link Response.Status#BAD_REQUEST BAD_REQUEST} when user JSON is not valid
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
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

        if(IGroupBean.GROUP_ALREDY_EXISTS_ERROR_CODE.equals(result)) {
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON)
                    .entity(GROUP_ALREADY_EXISTS_JSON).build();
        }

        return Response.ok().build();
    }

}
