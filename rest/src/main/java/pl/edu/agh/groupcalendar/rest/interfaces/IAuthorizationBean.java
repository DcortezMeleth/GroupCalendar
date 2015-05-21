package pl.edu.agh.groupcalendar.rest.interfaces;

import javax.ejb.Local;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * @author Bartosz
 *         Created on 2015-05-12.
 */

@Local
@Path("/")
public interface IAuthorizationBean {

    @GET
    @Path("date")
    @Produces("text/plain")
    String getDate();

    @GET
    @Path("param/{param}")
    @Produces("text/plain")
    String getParam(@PathParam("param") String param);
}
