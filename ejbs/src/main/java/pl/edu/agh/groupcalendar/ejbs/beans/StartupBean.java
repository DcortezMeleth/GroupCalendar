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

    @Resource(mappedName = "java:jboss/datasources/postgresDS")
    private DataSource ds;

    @Resource(mappedName = "java:jboss/datasources/group_calendar")
    private DataSource ds2;

    @PostConstruct
    public void init() {
        String methodName = "[init] ";
        LOGGER.info(methodName + "START");

        //inicjalizujemy logi
        InitLog4j.initLog4j(log4jConfigPath, log4jConfigFileName);
        LOGGER.info(methodName + "Log4j initialized!");

        /*try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.error("MySQL driver not found!", e);
        }*/

        try (Connection con = ds.getConnection(); Connection con2 = ds2.getConnection()){
            LOGGER.info("DB connected");

            LOGGER.info("Driver: " + con.getMetaData().getDriverName() + " " + con.getMetaData().getDriverVersion());
            LOGGER.info("Driver 2: " + con2.getMetaData().getDriverName() + " " + con2.getMetaData().getDriverVersion());

            //Connection conUnwrapped = con.unwrap(Connection.class);
            //Connection conUnwrapped2 = con2.unwrap(Connection.class);

            //LOGGER.info("Con1: " + conUnwrapped.getSchema());
            //LOGGER.info("Con2: " + conUnwrapped2.getSchema());
        } catch (SQLException e) {
            LOGGER.error("Cannot get connection to DB!", e);
        }

        LOGGER.info(methodName + "STOP");
    }


}
