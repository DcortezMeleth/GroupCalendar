package pl.edu.agh.groupcalendar.restservices.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.groupcalendar.ejbs.interfaces.IAuthBean;
import pl.edu.agh.groupcalendar.restservices.services.LoginService;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Request filer. <br>
 * Checks for session key in header. <br>
 * Allows for requests without key only to login, register and status service.
 *
 * @author Bartosz
 *         Created on 2015-05-25.
 */
@Provider
@PreMatching
public class SecurityRequestFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = LogManager.getLogger(SecurityRequestFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        LOGGER.info("Filtering request path: " + path);
        System.out.println("Filtering request path: " + path);

        //In case of browser tests
        if("OPTIONS".equals(requestContext.getMethod())) {
            requestContext.abortWith(Response.status(Response.Status.OK).build());
            return;
        }

        IAuthBean authBean = null;
        try {
            InitialContext context = new InitialContext();
            authBean = (IAuthBean) context.lookup("java:global/ear-1.0/ejbs-1.0/AuthBean!pl.edu.agh.groupcalendar.ejbs.interfaces.IAuthBean");
        } catch (NamingException e) {
            LOGGER.error("Cannot retrieve auth bean!", e);
        }

        if(authBean == null) {
            requestContext.abortWith(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
            return;
        }

        //Check if service key exists and is valid
        String serviceKey = requestContext.getHeaderString(LoginService.SERVICE_KEY);
        if(!authBean.validateServiceKey(serviceKey)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        //For other methods besides login
        if(!path.startsWith("/auth/login") && !path.startsWith("/auth/register") && !path.startsWith("/auth/status")) {
            String authToken = requestContext.getHeaderString(LoginService.SESSION_KEY);

            if(!authBean.validateSessionKey(authToken)) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        }
    }
}
