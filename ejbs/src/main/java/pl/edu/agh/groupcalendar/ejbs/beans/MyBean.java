package pl.edu.agh.groupcalendar.ejbs.beans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.groupcalendar.ejbs.interfaces.IMyBean;
import pl.edu.agh.groupcalendar.utils.User;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.jws.soap.SOAPBinding;
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
public class MyBean implements IMyBean {

    private static final Logger LOGGER = LogManager.getLogger(MyBean.class);

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
    public void insertMockUser() {
        User user = new User();
        user.setUs_name("Bartosz");
        user.setUs_surname("Sadel");
        user.setUs_username("Dcortez");
        user.setUs_email("bsadel69@gmail.com");
        user.setUs_password("dupa");
        entityManager.persist(user);
    }

    @Override
    public List<User> getUsers() {
        String q = "SELECT p from gc_users p";
        Query query = entityManager.createQuery(q);
        return query.getResultList();
    }


}
