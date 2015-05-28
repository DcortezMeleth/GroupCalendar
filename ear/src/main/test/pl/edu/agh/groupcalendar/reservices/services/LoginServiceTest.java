package pl.edu.agh.groupcalendar.reservices.services;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jboss.resteasy.util.HttpResponseCodes;
import org.junit.Assert;
import org.junit.Test;

import java.lang.Exception;

/**
 * Tests for
 *
 * @author Bartosz
 *         Created on 2015-05-28.
 */
public class LoginServiceTest {

    @Test
    public void testStatus() throws Exception {
        HttpClient httpClient = new HttpClient();
        GetMethod get = new GetMethod("http://localhost:8080/rest/auth/status");
        int status = httpClient.executeMethod(get);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);
        Assert.assertEquals("{\"status\":\"Service is running...\"}", get.getResponseBodyAsString());

        get.releaseConnection();
    }

}
