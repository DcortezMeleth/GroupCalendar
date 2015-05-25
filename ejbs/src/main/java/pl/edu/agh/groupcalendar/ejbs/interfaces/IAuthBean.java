package pl.edu.agh.groupcalendar.ejbs.interfaces;

import pl.edu.agh.groupcalendar.dto.User;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Bartosz
 *         Created on 2015-04-27.
 */
@Local
public interface IAuthBean {

    String NO_SUCH_USER_ERROR_CODE = "-1";
    String WRONG_PASSWORD_ERROR_CODE = "-2";

    /**
     * Login to system.
     * @param credentials encoded credentials
     * @return session key if login succeed, null otherwise.
     */
    String login(final String credentials);

    /**
     * Logout user form system.
     * @param sessionKey active session key
     * @return true if succeeded, false if session does not exists
     */
    boolean logout(final String sessionKey, final String username);

    void insertUser(final User user);

    List<User> getUsers();
}
