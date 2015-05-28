package pl.edu.agh.groupcalendar.ejbs.interfaces;

import pl.edu.agh.groupcalendar.dto.User;

import javax.ejb.Local;

/**
 * Authentication services facade.
 *
 * @author Bartosz
 *         Created on 2015-04-27.
 */
@Local
public interface IAuthBean {

    /** Error code - user does not exists. */
    String NO_SUCH_USER_ERROR_CODE = "-1";

    /** Error code - wrong password. */
    String WRONG_PASSWORD_ERROR_CODE = "-2";

    /** Error code - user with such username already exists. */
    String USERNAME_EXISTS_ERROR_CODE = "-4";

    /** Error code - user with such email already exists. */
    String EMAIL_EXISTS_ERROR_CODE = "-5";

    /**
     * Login to system.
     *
     * @param credentials encoded credentials
     * @return session key if login succeed, {@link #NO_SUCH_USER_ERROR_CODE} when user does not exists
     *      or {@link #WRONG_PASSWORD_ERROR_CODE} when user entered wrong password
     */
    String login(final String credentials);

    /**
     * Logout user form system.
     *
     * @param sessionKey active session key
     * @return true if succeeded, false if session does not exists
     */
    boolean logout(final String sessionKey, final String username);

    /**
     * Check if service key is correct.
     *
     * @param serviceKey serviceKey
     * @return true if correct, false otherwise
     */
    boolean validateServiceKey(final String serviceKey);

    /**
     * Check if session is valid.
     *
     * @param sessionKey active session key
     * @return true if valid, false otherwise
     */
    boolean validateSessionKey(final String sessionKey);

    /**
     * Register user.
     *
     * @param user newly created user
     * @return "0" if registered, {@link #USERNAME_EXISTS_ERROR_CODE} if username already taken
     *      or {@link #EMAIL_EXISTS_ERROR_CODE} if user with this email already exists
     */
    String register(final User user);
}
