package pl.edu.agh.groupcalendar.ejbs.beans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.groupcalendar.utils.InitLog4j;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * @author Bartosz
 *         Created on 2015-04-27.
 */
@Startup
@Singleton
public class StartupBean {

    private static final Logger LOGGER = LogManager.getLogger(StartupBean.class);

    @PostConstruct
    public void init() {
        String methodName = "[init] ";
        LOGGER.info(methodName + "START");

        //inicjalizujemy logi
        InitLog4j.initLog4j();

        //TODO:test polaczenia z baza danych

        LOGGER.info(methodName + "STOP");
    }


}
