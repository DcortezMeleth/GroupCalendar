package pl.edu.agh.groupcalendar.ejbs.interfaces;

import pl.edu.agh.groupcalendar.utils.User;

import javax.ejb.Local;
import java.util.Date;
import java.util.List;

/**
 * @author Bartosz
 *         Created on 2015-04-27.
 */
@Local
public interface IMyBean {

    Date getCurrentDate();

    void insertMockUser();

    List<User> getUsers();
}
