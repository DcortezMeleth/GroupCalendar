package pl.edu.agh.groupcalendar.reservices.services;

/**
 * Constants used in tests. <br>
 * Mainly URLs.
 *
 * @author Bartosz
 *         Created on 2015-05-28.
 */
public interface TestConstants {

    /** Service key header tag. */
    String SERVICE_KEY = "service_key";

    /** Auth token header tag. */
    String AUTH_TOKEN = "auth_token";

    /** Mock service key for mobile application. */
    String MOCK_SERVICE_KEY = "987456321";

    /** Base path for rest services. */
    String BASE_URL = "http://localhost:8080/rest/";

    /** Path for login rest services. */
    String LOGIN_SERVICE = BASE_URL + "auth/";
}
