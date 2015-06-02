package pl.edu.agh.groupcalendar.reservices.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.jboss.resteasy.util.HttpResponseCodes;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.edu.agh.groupcalendar.dto.Group;
import pl.edu.agh.groupcalendar.restservices.services.GroupService;
import pl.edu.agh.groupcalendar.restservices.services.LoginService;

import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Test suite for {@link GroupService}.
 *
 * @author Bartosz
 *         Created on 2015-06-01.
 */
public class GroupServiceTest {

    /** String with JSON for creating user. */
    private static final String USER_JSON = "{\"us_username\":\"groupTest\"," +
            "\"us_name\": \"Bartosz1\",\"us_surname\": \"Sadel1\",\"us_password\": \"dupa1\"," +
            "\"us_salt\": \"\",\"us_email\":\"groupTest@gmail.com\"}";

    /** String with JSON for creating user. */
    private static final String USER_JSON_2 = "{\"us_username\":\"groupTest2\"," +
            "\"us_name\": \"Bartosz1\",\"us_surname\": \"Sadel1\",\"us_password\": \"dupa1\"," +
            "\"us_salt\": \"\",\"us_email\":\"groupTest2@gmail.com\"}";

    /** Gson instance to allow easy JSON conversion. */
    private static Gson gson;

    /** Http client. */
    private static HttpClient httpClient;

    /** Session key for user used in test suite. */
    private static String sessionKey;

    /** Session key for user used in test suite. */
    private static String sessionKey2;

    /** Credentials of user used in test suite. */
    private static final String credentials = new String(Base64.encodeBase64("groupTest:dupa1".getBytes()));

    /** Credentials of user used in test suite. */
    private static final String credentials2 = new String(Base64.encodeBase64("groupTest2:dupa1".getBytes()));

    /**
     * Register user and login.
     *
     * @throws Exception
     */
    @BeforeClass
    public static void initUser() throws Exception {
        gson = new Gson();
        httpClient = new HttpClient();

        //rejestracja uzytkownika - powinna sie udac
        PostMethod register = new PostMethod(TestConstants.LOGIN_SERVICE + "register");
        register.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        register.setRequestEntity(new StringRequestEntity(USER_JSON, MediaType.APPLICATION_JSON, null));
        int status = httpClient.executeMethod(register);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        register.releaseConnection();

        //rejestracja uzytkownika - powinna sie udac
        register.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        register.setRequestEntity(new StringRequestEntity(USER_JSON_2, MediaType.APPLICATION_JSON, null));
        status = httpClient.executeMethod(register);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        register.releaseConnection();


        //logowanie - powinno sie udac
        PostMethod login = new PostMethod(TestConstants.LOGIN_SERVICE + "login/" + credentials);
        login.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        status = httpClient.executeMethod(login);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        Properties result = gson.fromJson(login.getResponseBodyAsString(), Properties.class);
        sessionKey = result.getProperty(LoginService.SESSION_KEY);
        Assert.assertNotNull(sessionKey);

        login.releaseConnection();


        //logowanie - powinno sie udac
        login = new PostMethod(TestConstants.LOGIN_SERVICE + "login/" + credentials2);
        login.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        status = httpClient.executeMethod(login);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        result = gson.fromJson(login.getResponseBodyAsString(), Properties.class);
        sessionKey2 = result.getProperty(LoginService.SESSION_KEY);
        Assert.assertNotNull(sessionKey);

        login.releaseConnection();
    }

    /**
     * Remove user.
     *
     * @throws Exception
     */
    @AfterClass
    public static void removeUser() throws Exception {
        //usuniecie uzytkownika - powinno sie udac
        PostMethod delete = new PostMethod(TestConstants.LOGIN_SERVICE + "delete/" + credentials);
        delete.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        delete.setRequestHeader(LoginService.SESSION_KEY, sessionKey);
        delete.setRequestEntity(new StringRequestEntity(USER_JSON, MediaType.APPLICATION_JSON, null));
        int status = httpClient.executeMethod(delete);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        delete.releaseConnection();
        //usuniecie uzytkownika - powinno sie udac
        delete = new PostMethod(TestConstants.LOGIN_SERVICE + "delete/" + credentials2);
        delete.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        delete.setRequestHeader(LoginService.SESSION_KEY, sessionKey2);
        delete.setRequestEntity(new StringRequestEntity(USER_JSON, MediaType.APPLICATION_JSON, null));
        status = httpClient.executeMethod(delete);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        delete.releaseConnection();
    }

    @Test
    public void flowTest() throws Exception {
        Group group1 = new Group();
        group1.setGr_name("GROUP 1");
        group1.setGr_desc("Group 1 description.");
        Group group2 = new Group();
        group2.setGr_name("GROUP 2");
        group2.setGr_desc("Group 2 description.");
        Group group3 = new Group();
        group3.setGr_name("GROUP 3");
        group3.setGr_desc("Group 3 description.");

        //tworzenie grupy 1 - powinno sie udac
        PostMethod create = new PostMethod(TestConstants.GROUP_SERVICE + "create");
        create.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        create.setRequestHeader(LoginService.SESSION_KEY, sessionKey);
        create.setRequestEntity(new StringRequestEntity(gson.toJson(group1), MediaType.APPLICATION_JSON, null));
        int status = httpClient.executeMethod(create);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        create.releaseConnection();

        //tworzenie grupy 2 - powinno sie udac
        create.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        create.setRequestHeader(LoginService.SESSION_KEY, sessionKey);
        create.setRequestEntity(new StringRequestEntity(gson.toJson(group2), MediaType.APPLICATION_JSON, null));
        status = httpClient.executeMethod(create);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        create.releaseConnection();

        //tworzenie grupy 3 - powinno sie udac
        create.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        create.setRequestHeader(LoginService.SESSION_KEY, sessionKey);
        create.setRequestEntity(new StringRequestEntity(gson.toJson(group3), MediaType.APPLICATION_JSON, null));
        status = httpClient.executeMethod(create);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        create.releaseConnection();

        //tworzenie grupy 3 po raz 2 - nie powinno sie udac
        create.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        create.setRequestHeader(LoginService.SESSION_KEY, sessionKey);
        create.setRequestEntity(new StringRequestEntity(gson.toJson(group3), MediaType.APPLICATION_JSON, null));
        status = httpClient.executeMethod(create);

        Assert.assertEquals(HttpResponseCodes.SC_CONFLICT, status);

        create.releaseConnection();

        //dolaczenie do grupy1 - user2
        PostMethod join = new PostMethod(TestConstants.GROUP_SERVICE + "join");
        join.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        join.setRequestHeader(LoginService.SESSION_KEY, sessionKey2);
        join.addParameter("groupname", group1.getGr_name());
        join.addParameter("username", "groupTest2");
        status = httpClient.executeMethod(join);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        join.releaseConnection();

        //dolaczenie do grupy2 - user2
        join.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        join.setRequestHeader(LoginService.SESSION_KEY, sessionKey2);
        join.addParameter("groupname", group2.getGr_name());
        join.addParameter("username", "groupTest2");
        status = httpClient.executeMethod(join);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        join.releaseConnection();

        //edycja grupy 1
        group3.setGr_desc("testt");
        PostMethod edit = new PostMethod(TestConstants.GROUP_SERVICE + "edit");
        edit.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        edit.setRequestHeader(LoginService.SESSION_KEY, sessionKey);
        edit.setRequestEntity(new StringRequestEntity(gson.toJson(group3), MediaType.APPLICATION_JSON, null));
        status = httpClient.executeMethod(edit);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        edit.releaseConnection();

        //pobranie listy grup - powinno zwrocic 2
        GetMethod getList = new GetMethod(TestConstants.GROUP_SERVICE + "groupList");
        getList.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        getList.setRequestHeader(LoginService.SESSION_KEY, sessionKey2);
        status = httpClient.executeMethod(getList);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        Type listType = new TypeToken<ArrayList<Group>>() {
        }.getType();
        List<Group> groups = gson.fromJson(getList.getResponseBodyAsString(), listType);

        Assert.assertNotNull(groups);
        Assert.assertEquals(groups.size(), 2);

        getList.releaseConnection();

        //odejscie z grupy - powinno sie udac
        PostMethod leave = new PostMethod(TestConstants.GROUP_SERVICE + "leave");
        leave.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        leave.setRequestHeader(LoginService.SESSION_KEY, sessionKey2);
        leave.addParameter("groupname", group1.getGr_name());
        leave.addParameter("username", "groupTest2");
        status = httpClient.executeMethod(leave);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        leave.releaseConnection();

        //pobranie listy grup - powinno zwrocic 1
        getList = new GetMethod(TestConstants.GROUP_SERVICE + "groupList");
        getList.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        getList.setRequestHeader(LoginService.SESSION_KEY, sessionKey2);
        status = httpClient.executeMethod(getList);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        groups = gson.fromJson(getList.getResponseBodyAsString(), listType);
        Assert.assertNotNull(groups);
        Assert.assertEquals(groups.size(), 1);

        getList.releaseConnection();

        //dolaczenie do grupy2 - user1 - dodanie przez admina - powinno sie udac
        PostMethod addToGroup = new PostMethod(TestConstants.GROUP_SERVICE + "join");
        addToGroup.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        addToGroup.setRequestHeader(LoginService.SESSION_KEY, sessionKey);
        addToGroup.addParameter("groupname", group1.getGr_name());
        addToGroup.addParameter("username", "groupTest2");
        status = httpClient.executeMethod(addToGroup);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        addToGroup.releaseConnection();

        //pobranie listy grup - powinno zwrocic 2
        getList = new GetMethod(TestConstants.GROUP_SERVICE + "groupList");
        getList.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        getList.setRequestHeader(LoginService.SESSION_KEY, sessionKey2);
        status = httpClient.executeMethod(getList);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        groups = gson.fromJson(getList.getResponseBodyAsString(), listType);
        Assert.assertNotNull(groups);
        Assert.assertEquals(groups.size(), 2);

        getList.releaseConnection();

        //usuniecie z grupy - powinno sie udac
        PostMethod removeFromGroup = new PostMethod(TestConstants.GROUP_SERVICE + "leave");
        removeFromGroup.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        removeFromGroup.setRequestHeader(LoginService.SESSION_KEY, sessionKey);
        removeFromGroup.addParameter("groupname", group1.getGr_name());
        removeFromGroup.addParameter("username", "groupTest2");
        status = httpClient.executeMethod(removeFromGroup);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        removeFromGroup.releaseConnection();

        //pobranie listy grup - powinno zwrocic 1
        getList = new GetMethod(TestConstants.GROUP_SERVICE + "groupList");
        getList.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        getList.setRequestHeader(LoginService.SESSION_KEY, sessionKey2);
        status = httpClient.executeMethod(getList);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        groups = gson.fromJson(getList.getResponseBodyAsString(), listType);
        Assert.assertNotNull(groups);
        Assert.assertEquals(groups.size(), 1);

        getList.releaseConnection();

        //usuniecie grupy 1
        PostMethod remove = new PostMethod(TestConstants.GROUP_SERVICE + "edit");
        remove.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        remove.setRequestHeader(LoginService.SESSION_KEY, sessionKey);
        remove.addParameter("del", "true");
        edit.setRequestEntity(new StringRequestEntity(gson.toJson(group1), MediaType.APPLICATION_JSON, null));
        status = httpClient.executeMethod(remove);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        remove.releaseConnection();

        //usuniecie grupy 2
        remove = new PostMethod(TestConstants.GROUP_SERVICE + "edit");
        remove.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        remove.setRequestHeader(LoginService.SESSION_KEY, sessionKey);
        remove.addParameter("del", "true");
        edit.setRequestEntity(new StringRequestEntity(gson.toJson(group2), MediaType.APPLICATION_JSON, null));
        status = httpClient.executeMethod(remove);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        remove.releaseConnection();

        //usuniecie grupy 3 - nie powinno sie udac
        remove = new PostMethod(TestConstants.GROUP_SERVICE + "edit");
        remove.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        remove.setRequestHeader(LoginService.SESSION_KEY, sessionKey2);
        remove.addParameter("del", "true");
        edit.setRequestEntity(new StringRequestEntity(gson.toJson(group3), MediaType.APPLICATION_JSON, null));
        status = httpClient.executeMethod(remove);

        Assert.assertEquals(HttpResponseCodes.SC_CONFLICT, status);

        remove.releaseConnection();

        //usuniecie grupy 3
        remove = new PostMethod(TestConstants.GROUP_SERVICE + "edit");
        remove.setRequestHeader(LoginService.SERVICE_KEY, TestConstants.MOCK_SERVICE_KEY);
        remove.setRequestHeader(LoginService.SESSION_KEY, sessionKey);
        remove.addParameter("del", "true");
        edit.setRequestEntity(new StringRequestEntity(gson.toJson(group3), MediaType.APPLICATION_JSON, null));
        status = httpClient.executeMethod(remove);

        Assert.assertEquals(HttpResponseCodes.SC_OK, status);

        remove.releaseConnection();


    }
}
