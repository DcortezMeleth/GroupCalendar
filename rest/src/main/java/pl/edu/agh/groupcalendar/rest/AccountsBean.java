package pl.edu.agh.groupcalendar.rest;

//import pl.edu.agh.groupcalendar.ejbs.interfaces.IMyBean;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

/**
 * @author Bartosz
 *         Created on 2015-05-12.
 */

@Stateless
@Path("accounts")
public class AccountsBean {

    //@EJB
    //private IMyBean myBean;

    public String getDate() {
        return null;//myBean.getCurrentDate().toString();
    }
}
