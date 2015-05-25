package pl.edu.agh.groupcalendar.ejbs.beans;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.groupcalendar.dto.Session;
import pl.edu.agh.groupcalendar.ejbs.interfaces.IAuthBean;
import pl.edu.agh.groupcalendar.dto.User;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * Bean serving authentication services.
 * @author Bartosz
 *         Created on 2015-04-27.
 */
@Stateless(name = "MyBean")
public class AuthBean implements IAuthBean {

    /** Logger. */
    private static final Logger LOGGER = LogManager.getLogger(AuthBean.class);

    /** Mock service key for mobile application. */
    private static final String MOCK_SERVICE_KEY = "987456321";

    @PersistenceContext(unitName = "group_calendar")
    private EntityManager entityManager;

    @PostConstruct
    private void test() {
        LOGGER.info("Encoded:" + Base64.encode("Dcortez:dupa".getBytes()));
    }

    @Override
    public String login(String credentials) {
        String usernameAndPassword = new String(Base64.decode(credentials));
        StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        String username = tokenizer.nextToken();
        String password = tokenizer.nextToken();

        Query query = entityManager.createQuery(User.GET_USER_BY_USERNAME).setParameter("username", username);
        User user = (User) query.getSingleResult();

        //jesli nie ma uzytkownika na bazie zwracamy null
        if(user == null) {
            return NO_SUCH_USER_ERROR_CODE;
        }

        //jesli haslo nie istnieje lub jest niepoprawne zwracamy null
        String dbPassword = user.getUs_password();
        if(dbPassword == null || !dbPassword.equals(password)) {
            return WRONG_PASSWORD_ERROR_CODE;
        }

        //tworzymy nowa sesje i zapisujemy na bazie
        Session session = new Session();
        session.setUser(user);
        session.setSs_last_action(new Date());
        session.setSs_key(UUID.randomUUID().toString());
        entityManager.persist(session);

        //zwracamy klucz sesji
        return session.getSs_key();
    }

    @Override
    public boolean logout(final String sessionKey, final String username) {
        Query getUserQuery = entityManager.createQuery(User.GET_USER_BY_USERNAME).setParameter("username", username);
        User user = (User) getUserQuery.getSingleResult();

        if(user == null) {
            return false;
        }

        Query getSessionQuery = entityManager.createQuery(Session.GET_SESSION_BY_USER_ID_AND_SESSION_KEY)
                .setParameter("us_id", user.getUs_id()).setParameter("ss_key", sessionKey);
        Session session = (Session) getSessionQuery.getSingleResult();

        if(session == null) {
            return false;
        }

        entityManager.getTransaction().begin();
        entityManager.remove(session);
        entityManager.getTransaction().commit();

        return true;
    }

    @Override
    public boolean validateServiceKey(final String serviceKey) {
        return MOCK_SERVICE_KEY.equals(serviceKey);
    }

    @Override
    public boolean validateSessionKey(final String sessionKey) {
        Query query = entityManager.createQuery(Session.GET_SESSION_BY_SESSION_KEY).setParameter("ss_key", sessionKey);
        Session session = (Session) query.getSingleResult();

        return session != null;
    }

    @Override
    public void insertUser(final User user) {
        entityManager.persist(user);
    }

    @Override
    public List<User> getUsers() {
        Query query = entityManager.createQuery(User.GET_ALL_USERS);
        return query.getResultList();
    }


}
