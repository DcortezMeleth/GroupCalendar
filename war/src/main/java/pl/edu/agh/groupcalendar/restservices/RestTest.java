package pl.edu.agh.groupcalendar.restservices;

import pl.edu.agh.groupcalendar.ejbs.interfaces.IMyBean;
import pl.edu.agh.groupcalendar.utils.User;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author Bartosz
 *         Created on 2015-05-21.
 */
@Path("/test")
@Stateless
public class RestTest {

    @EJB
    private IMyBean myBean;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/status")
    public Response getStatus() {
        return Response.ok("{\"status\":\"Service is running...\"}").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/status/{param}")
    public Response getStatusParam(@PathParam("param") String param) {
        return Response.ok("{\"status\":\"Service is running...\"," +
                "\"param\":\"" + param + "\"}").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/bean")
    public Response beanTest() {
        return Response.ok("{\"status\":\"Service is running...\"," +
                "\"date\":\"" + myBean.getCurrentDate() + "\"}").build();
    }

    @PUT
    @Path("/user")
    public void setUser() {
        myBean.insertMockUser();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users")
    public Response getUsers() {
        List<User> users = myBean.getUsers();
        String usersString = "";
        for(User user : users) {
            usersString += user.toString() + ", ";
        }

        return Response.ok("{\"status\":\"ok\"," +
                "\"users\":\"" + usersString + "\"}").build();
    }
}
