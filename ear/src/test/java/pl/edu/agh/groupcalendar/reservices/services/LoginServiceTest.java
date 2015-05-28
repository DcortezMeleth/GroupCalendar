package pl.edu.agh.groupcalendar.reservices.services;

import com.google.gson.Gson;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.jboss.resteasy.util.HttpResponseCodes;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.groupcalendar.restservices.services.LoginService;

import javax.ws.rs.core.MediaType;
import java.lang.Exception;
import java.util.Properties;

/**
 * Test suite for {@link LoginService}.
 *
 * @author Bartosz
 *         Created on 2015-05-28.
 */
public class LoginServiceTest {

    /** String witg JSON for creating user. */
    private static final String USER_JSON = "{\"us_username\":\"Dcortez1\"," +
            "\"us_name\": \"Bartosz1\",\"us_surname\": \"Sadel1\",\"us_password\": \"dupa1\"," +
            "\"us_salt\": \"\",\"us_email\":\"bsadel691@gmail.com\"}";

    /** Gson instance to allow easy JSON conversion. */
    private Gson gson;

    /** Http client.*/
    private HttpClient httpClient;

    @Before
    public void init() {
        gson = new Gson();
        httpClient = new HttpClient();
    }

    @Test
    public void testStatus() throws Exception {
        GetMethod get = new GetMethod(TestConstants.LOGIN_SERVICE + "status");
        get.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        int status = httpClient.executeMethod(get);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);
        Assert.assertEquals("{\"status\":\"Service is running...\"}", get.getResponseBodyAsString());

        get.releaseConnection();
    }

    @Test
    public void registerAndLoginTest() throws Exception {
        PostMethod register = new PostMethod(TestConstants.LOGIN_SERVICE + "register");
        register.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        register.setRequestEntity(new StringRequestEntity(USER_JSON, MediaType.APPLICATION_JSON, null));
        int status = httpClient.executeMethod(register);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        register.releaseConnection();

        String credentials = new String(Base64.encodeBase64("Dcortez1:dupa1".getBytes()));
        PostMethod login = new PostMethod(TestConstants.LOGIN_SERVICE + "login/" + credentials);
        login.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        status = httpClient.executeMethod(login);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        Properties result = gson.fromJson(login.getResponseBodyAsString(), Properties.class);
        String sessionKey = result.getProperty(LoginService.SESSION_KEY);
        Assert.assertNotNull(sessionKey);

        login.releaseConnection();

        PostMethod logout = new PostMethod(TestConstants.LOGIN_SERVICE + "logout/Dcortez1");
        logout.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        status = httpClient.executeMethod(logout);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        logout.releaseConnection();
    }

}
