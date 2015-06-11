package pl.edu.agh.groupcalendar.ejbs.beans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.groupcalendar.dto.Event;
import pl.edu.agh.groupcalendar.dto.Group;
import pl.edu.agh.groupcalendar.dto.Session;
import pl.edu.agh.groupcalendar.dto.User;
import pl.edu.agh.groupcalendar.ejbs.interfaces.IEventBean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Bean serving event related services.
 *
 * @author Bartosz
 *         Created on 2015-06-08.
 */
@Stateless
public class EventBean implements IEventBean {

    /** Logger. */
    private static final Logger LOGGER = LogManager.getLogger(GroupBean.class);

    @PersistenceContext(unitName = "group_calendar")
    private EntityManager entityManager;

    @Override
    public String create(final Event event, final String groupName, final String sessionKey) {
        TypedQuery<Group> groupQuery = entityManager.createQuery(Group.GET_GROUP_BY_NAME, Group.class).setParameter
                ("name", groupName);

        if(groupQuery.getResultList().isEmpty()) {
            LOGGER.info("Cannot create event. Group does not exists.");
            return GROUP_DOES_NOT_EXISTS_ERROR_CODE;
        }

        Group group = groupQuery.getSingleResult();

        TypedQuery<Session> query = entityManager.createQuery(Session.GET_SESSION_BY_SESSION_KEY, Session.class)
                .setParameter("ss_key", sessionKey);
        Session session = query.getSingleResult();

        User user = session.getUser();

        if(!user.equals(group.getGr_admin())) {
            LOGGER.info("Cannot create event. No sufficient rights.");
            return NO_RIGHTS_ERROR_CODE;
        }

        group.addEvent(event);
        event.setGroup(group);

        entityManager.persist(event);

        LOGGER.info("Event created: " + event.getEv_name());
        return SUCCESS;
    }

    @Override
    public String modify(final Event event, final String sessionKey, final boolean remove) {
        TypedQuery<Event> eventQuery = entityManager.createQuery(Event.GET_EVENT_BY_NAME, Event.class).setParameter
                ("name", event.getEv_name());

        if(eventQuery.getResultList().isEmpty()) {
            LOGGER.info("Cannot modify event. This event does not exists.");
            return EVENT_DOES_NOT_EXISTS_ERROR_CODE;
        }

        TypedQuery<Session> query = entityManager.createQuery(Session.GET_SESSION_BY_SESSION_KEY, Session.class)
                .setParameter("ss_key", sessionKey);
        Session session = query.getSingleResult();

        User user = session.getUser();

        if(!user.equals(event.getGroup().getGr_admin())) {
            LOGGER.info("Cannot modify event. Need admin rights!");
            return NO_RIGHTS_ERROR_CODE;
        }

        if(remove) {
            entityManager.remove(event);
        } else {
            entityManager.merge(event);
        }

        return SUCCESS;
    }

    @Override
    public List<Event> eventsList(final String groupName) {
        List<Event> result = new ArrayList<>();
        TypedQuery<Group> groupQuery = entityManager.createQuery(Group.GET_GROUP_BY_NAME, Group.class)
                .setParameter("name", groupName);

        if(groupQuery.getResultList().isEmpty()) {
            LOGGER.info("Cannot create event. Group does not exists.");
            return result;
        }

        Group group = groupQuery.getSingleResult();

        result = group.getEvents();

        return result;
    }
}
