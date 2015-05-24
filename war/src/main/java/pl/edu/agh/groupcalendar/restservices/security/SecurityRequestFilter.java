package pl.edu.agh.groupcalendar.restservices.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.groupcalendar.restservices.services.LoginService;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
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

        //In case of browser tests
        if("OPTIONS".equals(requestContext.getMethod())) {
            requestContext.abortWith(Response.status(Response.Status.OK).build());
            return;
        }

        //TODO: pobrac beana do autoryzacji i uzywac go

        //Check if service key exists and is valid
        String serviceKey = requestContext.getHeaderString(LoginService.SERVICE_KEY);
        if(!"testServiceKey".equals(serviceKey)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        //For other methods besides login
        if(!path.startsWith("/auth/login")) {
            String authToken = requestContext.getHeaderString(LoginService.AUTH_TOKEN);

            if(!"authoToken".equals(authToken)) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        }
    }
}
