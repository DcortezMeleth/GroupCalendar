package pl.edu.agh.groupcalendar.rest.interfaces;

import javax.ejb.Local;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author Bartosz
 *         Created on 2015-05-12.
 */

@Local
@Path("/auth")
public interface IAuthorizationBean {

    @GET
    @Path("/date")
    String getDate();

    @GET
    @Path("/param/{param}")
    String getParam(@PathParam("param") String param);
}
