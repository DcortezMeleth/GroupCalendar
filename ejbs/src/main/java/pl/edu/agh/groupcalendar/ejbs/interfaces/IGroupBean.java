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
    String SUCCES ="0";

    /** Error code - group with this name already exists. */
    String GROUP_ALREDY_EXISTS_ERROR_CODE = "-1";

    /**
     * Creates group.
     * @param group group to create
     * @return {@link #SUCCES} if created
     */
    String create(final Group group, final String sessionKey);

}
