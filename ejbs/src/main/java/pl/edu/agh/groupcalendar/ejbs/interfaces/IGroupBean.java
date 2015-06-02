package pl.edu.agh.groupcalendar.ejbs.interfaces;

import pl.edu.agh.groupcalendar.dto.Group;

import javax.ejb.Local;
import java.util.List;

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

    /** Error code - no rioght to modify. */
    String USER_DOES_NOT_EXISTS_ERROR_CODE = "-4";

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
     *      or {@link #GROUP_DOES_NOT_EXISTS_ERROR_CODE} when group with this name does not exists
     */
    String modify(final Group group, final String sessionKey, final boolean remove);

    /**
     * Adds user to group
     *
     * @param groupName name of group to join
     * @param userName name od user to add
     * @param sessionKey session key of a singed user
     * @return {@link #SUCCESS} if registered, {@link #USER_DOES_NOT_EXISTS_ERROR_CODE} when user does not exists
     *      ,{@link #GROUP_DOES_NOT_EXISTS_ERROR_CODE} if group with this name does not exists or
     *      {@link #NO_RIGHTS_ERROR_CODE} if requested by user without correct rights
     */
    String join(final String groupName, final String userName, final String sessionKey);

    /**
     * Removes user from group group
     *
     * @param groupName name of group to join
     * @param userName name od user to add
     * @param sessionKey session key of a singed user
     * @return {@link #SUCCESS} if registered, {@link #USER_DOES_NOT_EXISTS_ERROR_CODE} when user does not exists
     *      ,{@link #GROUP_DOES_NOT_EXISTS_ERROR_CODE} if group with this name does not exists or
     *      {@link #NO_RIGHTS_ERROR_CODE} if requested by user without correct rights
     */
    String leave(final String groupName, final String userName, final String sessionKey);


    /**
     * Returns groups for a user
     *
     * @param sessionKey session key of a singed user
     * @return list of groups to whom user belongs
     */
    List<Group> groupsList(final String sessionKey);
}
