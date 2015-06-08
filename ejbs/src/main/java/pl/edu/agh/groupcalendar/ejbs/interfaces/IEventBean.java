package pl.edu.agh.groupcalendar.ejbs.interfaces;

import pl.edu.agh.groupcalendar.dto.Event;

import javax.ejb.Local;
import java.util.List;

/**
 * Event services facade.
 *
 * @author Bartosz
 *         Created on 2015-06-08.
 */
@Local
public interface IEventBean {

    /** Error code - succes. */
    String SUCCESS = "0";

    /** Error code - group with this name already exists. */
    String GROUP_DOES_NOT_EXISTS_ERROR_CODE = "-1";

    /** Error code - no rioght to modify. */
    String NO_RIGHTS_ERROR_CODE = "-2";

    /** Error code - event with this name don't exists. */
    String EVENT_DOES_NOT_EXISTS_ERROR_CODE = "-3";

    /**
     * Creates group.
     *
     * @param event event to create
     * @param groupName group to which event belongs
     * @param sessionKey user session key
     * @return {@link #SUCCESS} if created
     */
    String create(final Event event, final String groupName, final String sessionKey);

    /**
     * Modifies or deletes group
     *
     * @param event event to create
     * @param sessionKey user session key
     * @param remove true if group to remove, false if to modify
     * @return {@link #SUCCESS} if registered, {@link #NO_RIGHTS_ERROR_CODE} if requested by user without correct rights
     *      or {@link #GROUP_DOES_NOT_EXISTS_ERROR_CODE} when group with this name does not exists
     */
    String modify(final Event event, final String sessionKey, final boolean remove);

    /**
     * Returns events for a group
     *
     * @param groupName name of group
     * @return list of groups to whom user belongs
     */
    List<Event> eventsList(final String groupName);
}
