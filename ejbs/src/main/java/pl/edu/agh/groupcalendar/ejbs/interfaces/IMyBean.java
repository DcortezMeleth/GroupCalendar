package pl.edu.agh.groupcalendar.ejbs.interfaces;

import javax.ejb.Local;
import java.util.Date;

/**
 * @author Bartosz
 *         Created on 2015-04-27.
 */
@Local
public interface IMyBean {

    Date getCurrentDate();

}
