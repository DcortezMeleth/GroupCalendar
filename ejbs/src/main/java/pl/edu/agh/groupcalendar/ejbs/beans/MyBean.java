package pl.edu.agh.groupcalendar.ejbs.beans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.groupcalendar.ejbs.interfaces.IMyBean;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import java.util.Date;

/**
 * @author Bartosz
 *         Created on 2015-04-27.
 */
@Stateless(name = "MyBean")
public class MyBean implements IMyBean {

    private static final Logger LOGGER = LogManager.getLogger(MyBean.class);

    @PostConstruct
    private void test() {
        LOGGER.debug("Bean zyje!");
    }

    @Override
    public Date getCurrentDate() {
        return new Date();
    }
}
