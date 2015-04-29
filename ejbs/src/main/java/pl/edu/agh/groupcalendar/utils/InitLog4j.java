package pl.edu.agh.groupcalendar.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Klasa inicjujaca logi w aplikacji.
 * @author Bartosz
 *         Created on 2015-04-28.
 */
public final class InitLog4j {

    /**
     * Konstruktor prywatny. Jest to klasa utilowa. Nie tworzymy jej instancji.
     */
    private InitLog4j() {
    }

    public static void initLog4j(String path, String fileName) {
        try {
            ConfigurationSource source = new ConfigurationSource(new FileInputStream(path + fileName));
            XmlConfiguration xmlConfiguration = new XmlConfiguration(source);
            Logger logger = (Logger) LogManager.getLogger();
            logger.getContext().start(xmlConfiguration);
        } catch (IOException e) {
            System.out.println("Blad przy inicjalizacji loggera!!!");
            e.printStackTrace();
        }
    }


}
