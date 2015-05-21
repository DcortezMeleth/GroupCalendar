package pl.edu.agh.groupcalendar.ejbs.beans;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.edu.agh.groupcalendar.utils.InitLog4j;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

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

    @Resource(mappedName = "java:/group_calendar")
    private DataSource ds;

    @PostConstruct
    public void init() {
        String methodName = "[init] ";
        LOGGER.info(methodName + "START");

        //inicjalizujemy logi
        InitLog4j.initLog4j(log4jConfigPath, log4jConfigFileName);
        LOGGER.info(methodName + "Log4j initialized!");

        try (Connection con = ds.getConnection()){
            LOGGER.info("DB connected");
            LOGGER.info("Schema: " + con.getSchema());
        } catch (SQLException e) {
            LOGGER.error("Cannot get connection to DB!", e);
        }

        LOGGER.info(methodName + "STOP");
    }


}
