package pl.edu.agh.groupcalendar.restservices.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.groupcalendar.restservices.services.LoginService;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Response filer. <br>
 * Adds appropriate headers.
 *
 * @author Bartosz
 *         Created on 2015-05-24.
 */
@Provider
@PreMatching
public class SecurityResponseFilter implements ContainerResponseFilter {

    private static final Logger LOGGER = LogManager.getLogger(SecurityResponseFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        LOGGER.info("Filtering REST Response");

        responseContext.getHeaders().add( "Access-Control-Allow-Origin", "*" );
        responseContext.getHeaders().add( "Access-Control-Allow-Credentials", "true" );
        responseContext.getHeaders().add( "Access-Control-Allow-Methods", "GET, POST, DELETE, PUT" );
        responseContext.getHeaders().add( "Access-Control-Allow-Headers", LoginService.SESSION_KEY + ", " + LoginService.SERVICE_KEY );
    }
}
