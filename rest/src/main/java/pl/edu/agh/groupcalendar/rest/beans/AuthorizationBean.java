package pl.edu.agh.groupcalendar.rest.beans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.groupcalendar.ejbs.interfaces.IMyBean;
import pl.edu.agh.groupcalendar.rest.interfaces.IAuthorizationBean;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.*;

/**
 * @author Bartosz
 *         Created on 2015-05-13.
 */
@LocalBean
@Path("/")
@Stateless
public class AuthorizationBean {//implements IAuthorizationBean {

    private final static Logger LOGGER = LogManager.getLogger(AuthorizationBean.class);

    @EJB
    private IMyBean myBean;

    @GET
    @Path("date")
    @Produces("text/plain")
    public String getDate() {
        LOGGER.debug("Service invoked! Called method getDate()");
        return myBean.getCurrentDate().toString();
    }

    @GET
    @Path("param/{param}")
    @Produces("text/plain")
    public String getParam(@PathParam("param")String param) {
        return param;
    }
}
