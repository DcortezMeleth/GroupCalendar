package pl.edu.agh.groupcalendar.ejbs.beans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.groupcalendar.dto.Group;
import pl.edu.agh.groupcalendar.dto.Session;
import pl.edu.agh.groupcalendar.dto.User;
import pl.edu.agh.groupcalendar.ejbs.interfaces.IGroupBean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Bean serving group connected services.
 *
 * @author Bartosz
 *         Created on 2015-05-30.
 */
@Stateless
public class GroupBean implements IGroupBean {

    /** Logger. */
    private static final Logger LOGGER = LogManager.getLogger(GroupBean.class);

    @PersistenceContext(unitName = "group_calendar")
    private EntityManager entityManager;

    @Override
    public String create(final Group group, final String sessionKey) {
        TypedQuery<Group> groupQuery = entityManager.createQuery(Group.GET_GROUP_BY_NAME, Group.class).setParameter
                ("name", group.getGr_name());

        if(!groupQuery.getResultList().isEmpty()) {
            LOGGER.info("Cannot create group. Name already taken.");
            return GROUP_ALREADY_EXISTS_ERROR_CODE;
        }

        TypedQuery<Session> query = entityManager.createQuery(Session.GET_SESSION_BY_SESSION_KEY, Session.class)
                .setParameter("ss_key", sessionKey);
        Session session = query.getSingleResult();

        User user = session.getUser();

        group.setGc_admin(user);

        entityManager.persist(group);

        LOGGER.info("Group created: " + group.getGr_name());
        return SUCCESS;
    }

    @Override
    public String modify(final Group group, final String sessionKey, final boolean remove) {
        TypedQuery<Group> groupQuery = entityManager.createQuery(Group.GET_GROUP_BY_NAME, Group.class).setParameter
                ("name", group.getGr_name());

        if(groupQuery.getResultList().isEmpty()) {
            LOGGER.info("Cannot modify group. This group does not exists.");
            return GROUP_DOES_NOT_EXISTS_ERROR_CODE;
        }

        TypedQuery<Session> query = entityManager.createQuery(Session.GET_SESSION_BY_SESSION_KEY, Session.class)
                .setParameter("ss_key", sessionKey);
        Session session = query.getSingleResult();

        User user = session.getUser();

        if(!user.equals(group.getGc_admin())) {
            LOGGER.info("Cannot modify group. Need admin rights!");
            return NO_RIGHTS_ERROR_CODE;
        }

        if(remove) {
            entityManager.remove(group);
        } else {
            entityManager.merge(group);
        }

        return SUCCESS;
    }

    @Override
    public String join(final String groupName, final String username, final String sessionKey) {
        TypedQuery<Group> groupQuery = entityManager.createQuery(Group.GET_GROUP_BY_NAME, Group.class).setParameter
                ("name", groupName);

        if(groupQuery.getResultList().isEmpty()) {
            LOGGER.info("Cannot add user to group. Group does not exists.");
            return GROUP_DOES_NOT_EXISTS_ERROR_CODE;
        }

        Group group = groupQuery.getSingleResult();

        TypedQuery<Session> query = entityManager.createQuery(Session.GET_SESSION_BY_SESSION_KEY, Session.class)
                .setParameter("ss_key", sessionKey);
        Session session = query.getSingleResult();

        User user = session.getUser();

        if(!user.equals(group.getGc_admin())) {
            LOGGER.info("Cannot add user to group. Need admin rights!");
            return NO_RIGHTS_ERROR_CODE;
        }

        TypedQuery<User> getUserQuery = entityManager.createQuery(User.GET_USER_BY_USERNAME, User.class).setParameter
                ("username", username);

        if(getUserQuery.getResultList().isEmpty()) {
            LOGGER.info("Cannot add user to group. User does not exists!");
            return USER_DOES_NOT_EXISTS_ERROR_CODE;
        }

        user = getUserQuery.getSingleResult();

        group.addUser(user);

        LOGGER.info("User: " + username + " added to group " + groupName);

        return SUCCESS;
    }

    @Override
    public String leave(final String groupName, final String username, final String sessionKey) {
        TypedQuery<Group> groupQuery = entityManager.createQuery(Group.GET_GROUP_BY_NAME, Group.class).setParameter
                ("name", groupName);

        if(groupQuery.getResultList().isEmpty()) {
            LOGGER.info("Cannot add user to group. Group does not exists.");
            return GROUP_DOES_NOT_EXISTS_ERROR_CODE;
        }

        Group group = groupQuery.getSingleResult();

        TypedQuery<Session> query = entityManager.createQuery(Session.GET_SESSION_BY_SESSION_KEY, Session.class)
                .setParameter("ss_key", sessionKey);
        Session session = query.getSingleResult();

        User user = session.getUser();

        TypedQuery<User> getUserQuery = entityManager.createQuery(User.GET_USER_BY_USERNAME, User.class).setParameter
                ("username", username);

        if(getUserQuery.getResultList().isEmpty()) {
            LOGGER.info("Cannot add user to group. User does not exists!");
            return USER_DOES_NOT_EXISTS_ERROR_CODE;
        }

        User userToRemove = getUserQuery.getSingleResult();

        //if not admin and not requested user - no rights!
        if(!user.equals(group.getGc_admin()) && !user.getUs_username().equals(username)) {
            LOGGER.info("Cannot add user to group. No sufficient rights!");
            return NO_RIGHTS_ERROR_CODE;
        }

        group.removeUser(userToRemove);

        LOGGER.info("User: " + username + " removed from group " + groupName);

        return SUCCESS;
    }
}
