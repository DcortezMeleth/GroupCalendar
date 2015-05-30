package pl.edu.agh.groupcalendar.ejbs.beans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.groupcalendar.dto.Session;
import pl.edu.agh.groupcalendar.dto.User;
import pl.edu.agh.groupcalendar.ejbs.interfaces.IAuthBean;

import org.apache.commons.codec.binary.Base64;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * Bean serving authentication services.
 *
 * @author Bartosz
 *         Created on 2015-04-27.
 */
@Stateless
public class AuthBean implements IAuthBean {

    /** Logger. */
    private static final Logger LOGGER = LogManager.getLogger(AuthBean.class);

    /** Mock service key for mobile application. */
    private static final String MOCK_SERVICE_KEY = "987456321";

    @PersistenceContext(unitName = "group_calendar")
    private EntityManager entityManager;

    @Override
    public String login(final String credentials) {
        String usernameAndPassword = new String(Base64.decodeBase64(credentials));
        StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        String username = tokenizer.nextToken();
        String password = tokenizer.nextToken();

        TypedQuery<User> query = entityManager.createQuery(User.GET_USER_BY_USERNAME, User.class)
                .setParameter("username", username);
        User user = query.getResultList().size() != 0 ? query.getSingleResult() : null;

        //jesli nie ma uzytkownika na bazie zwracamy null
        if (user == null) {
            return NO_SUCH_USER_ERROR_CODE;
        }

        //jesli haslo nie istnieje lub jest niepoprawne zwracamy null
        String dbPassword = user.getUs_password();
        if (dbPassword == null || !dbPassword.equals(password)) {
            return WRONG_CREDENTIALS_ERROR_CODE;
        }

        //tworzymy nowa sesje i zapisujemy na bazie
        Session session = new Session();
        session.setUser(user);
        session.setSs_last_action(new Date());
        session.setSs_key(UUID.randomUUID().toString());
        entityManager.persist(session);

        LOGGER.debug("User with id:" + user.getUs_id() + " logged in.");

        //zwracamy klucz sesji
        return session.getSs_key();
    }

    @Override
    public boolean logout(final String sessionKey, final String username) {
        TypedQuery<User> getUserQuery = entityManager.createQuery(User.GET_USER_BY_USERNAME, User.class)
                .setParameter("username", username);
        User user = getUserQuery.getResultList().size() != 0 ? getUserQuery.getSingleResult() : null;

        if (user == null) {
            return false;
        }

        LOGGER.debug("User with id:" + user.getUs_id() + " logged out.");

        TypedQuery<Session> getSessionQuery =
                entityManager.createQuery(Session.GET_SESSION_BY_USER_ID_AND_SESSION_KEY, Session.class)
                .setParameter("user", user).setParameter("sessionKey", sessionKey);
        Session session = getSessionQuery.getResultList().size() != 0 ? getSessionQuery.getSingleResult() : null;

        if (session == null) {
            return false;
        }

        entityManager.remove(session);
        return true;
    }

    @Override
    public boolean validateServiceKey(final String serviceKey) {
        return MOCK_SERVICE_KEY.equals(serviceKey);
    }

    @Override
    public boolean validateSessionKey(final String sessionKey) {
        TypedQuery<Session> query = entityManager.createQuery(Session.GET_SESSION_BY_SESSION_KEY, Session.class)
                .setParameter("ss_key", sessionKey);
        List<Session> result = query.getResultList();

        return result.size() == 1 && result.get(0).getSs_key().equals(sessionKey);
    }

    @Override
    public String register(final User user) {
        TypedQuery<User> query = entityManager.createQuery(User.GET_USER_BY_USERNAME_OR_EMAIL, User.class)
                .setParameter("username", user.getUs_username()).setParameter("email", user.getUs_email());

        if (query.getResultList().size() != 0) {
            LOGGER.debug("Cannot register user! Username or email already in db.");
            User user1 = query.getResultList().get(0);
            return user1.getUs_username().equals(user.getUs_username()) ? USERNAME_EXISTS_ERROR_CODE : EMAIL_EXISTS_ERROR_CODE;
        }

        entityManager.persist(user);

        LOGGER.info("User registered: " + user.getUs_username());
        return SUCCESS;
    }

    @Override
    public String deleteUser(final User user, final String credentials) {
        String usernameAndPassword = new String(Base64.decodeBase64(credentials));
        StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        String username = tokenizer.nextToken();
        String password = tokenizer.nextToken();

        TypedQuery<User> query = entityManager.createQuery(User.GET_USER_BY_USERNAME_OR_EMAIL, User.class)
                .setParameter("username", user.getUs_username()).setParameter("email", user.getUs_email());

        if (query.getResultList().size() == 0) {
            LOGGER.debug("Cannot delete user! User does not exists. Username: " + user.getUs_username());
            return NO_SUCH_USER_ERROR_CODE;
        }

        User userToRemove = query.getSingleResult();

        if(!userToRemove.getUs_username().equals(username) || !userToRemove.getUs_password().equals(password)) {
            LOGGER.debug("Cannot delete user! Wrong credentials. Username: " + user.getUs_username());
            return WRONG_CREDENTIALS_ERROR_CODE;
        }

        entityManager.remove(userToRemove);

        LOGGER.info("User removed: " + user.getUs_username());
        return SUCCESS;
    }
}
