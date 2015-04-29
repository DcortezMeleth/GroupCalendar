package pl.edu.agh.groupcalendar.ejbs.beans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.groupcalendar.utils.InitLog4j;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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

    @Resource(name = "log4j/configPath")
    private String log4jConfigPath;

    @Resource(name = "log4j/configFile")
    private String log4jConfigFileName;

    @PostConstruct
    public void init() {
        String methodName = "[init] ";
        LOGGER.info(methodName + "START");

        //inicjalizujemy logi
        InitLog4j.initLog4j(log4jConfigPath, log4jConfigFileName);

        //TODO:test polaczenia z baza danych

        LOGGER.info(methodName + "STOP");
    }


}
