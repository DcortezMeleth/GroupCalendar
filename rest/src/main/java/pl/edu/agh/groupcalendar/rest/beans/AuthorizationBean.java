package pl.edu.agh.groupcalendar.rest.beans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.groupcalendar.ejbs.interfaces.IMyBean;
import pl.edu.agh.groupcalendar.rest.interfaces.IAuthorizationBean;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * @author Bartosz
 *         Created on 2015-05-13.
 */
@Stateless
public class AuthorizationBean implements IAuthorizationBean {

    private final static Logger LOGGER = LogManager.getLogger(AuthorizationBean.class);

    @EJB
    private IMyBean myBean;

    @Override
    public String getDate() {
        LOGGER.debug("Service invoked! Called method getDate()");
        return myBean.getCurrentDate().toString();
    }

    @Override
    public String getParam(String param) {
        return param;
    }
}
