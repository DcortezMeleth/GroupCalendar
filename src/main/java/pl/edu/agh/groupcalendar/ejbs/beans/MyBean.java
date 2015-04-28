package pl.edu.agh.groupcalendar.ejbs.beans;

import pl.edu.agh.groupcalendar.ejbs.interfaces.IMyBean;

import javax.ejb.Stateless;

/**
 * @author Bartosz
 *         Created on 2015-04-27.
 */
@Stateless(name = "MyEJB")
public class MyBean implements IMyBean{
    public MyBean() {
    }
}
