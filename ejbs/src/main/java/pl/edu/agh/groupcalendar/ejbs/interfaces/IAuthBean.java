package pl.edu.agh.groupcalendar.ejbs.interfaces;

import pl.edu.agh.groupcalendar.dto.User;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Bartosz
 *         Created on 2015-04-27.
 */
@Local
public interface IAuthBean {

    void insertUser(final User user);

    List<User> getUsers();
}
