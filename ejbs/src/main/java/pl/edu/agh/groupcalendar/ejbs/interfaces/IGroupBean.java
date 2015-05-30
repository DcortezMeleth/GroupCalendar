package pl.edu.agh.groupcalendar.ejbs.interfaces;

import pl.edu.agh.groupcalendar.dto.Group;

import javax.ejb.Local;

/**
 * Group services facade.
 *
 * @author Bartosz
 *         Created on 2015-05-30.
 */
@Local
public interface IGroupBean {

    /** Error code - succes. */
    String SUCCESS = "0";

    /** Error code - group with this name already exists. */
    String GROUP_ALREADY_EXISTS_ERROR_CODE = "-1";

    /** Error code - group with this name already exists. */
    String GROUP_DOES_NOT_EXISTS_ERROR_CODE = "-2";

    /** Error code - no rioght to modify. */
    String NO_RIGHTS_ERROR_CODE = "-3";

    /**
     * Creates group.
     *
     * @param group      group to create
     * @param sessionKey user session key
     * @return {@link #SUCCESS} if created
     */
    String create(final Group group, final String sessionKey);


    /**
     * Modifies or deletes group
     *
     * @param group      group to create
     * @param sessionKey user session key
     * @param remove     true if group to remove, false if to modify
     * @return {@link #SUCCESS} if registered, {@link #NO_RIGHTS_ERROR_CODE} if requested by user without correct rights
     *      or {@link #GROUP_DOES_NOT_EXISTS_ERROR_CODE} if group with this name does not exists
     */
    String modify(final Group group, final String sessionKey, final boolean remove);
}
