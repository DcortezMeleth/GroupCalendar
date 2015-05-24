package pl.edu.agh.groupcalendar.ejbs.beans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.groupcalendar.ejbs.interfaces.IAuthBean;
import pl.edu.agh.groupcalendar.dto.User;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * @author Bartosz
 *         Created on 2015-04-27.
 */
@Stateless(name = "MyBean")
public class AuthBean implements IAuthBean {

    private static final Logger LOGGER = LogManager.getLogger(AuthBean.class);

    @PersistenceContext(unitName = "group_calendar")
    private EntityManager entityManager;

    @PostConstruct
    private void test() {
        LOGGER.debug("Bean zyje!");
    }

    @Override
    public Date getCurrentDate() {
        return new Date();
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
