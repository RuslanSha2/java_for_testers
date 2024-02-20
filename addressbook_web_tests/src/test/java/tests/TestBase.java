package tests;

import com.titusfortner.logging.SeleniumLogger;
import manager.ApplicationManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;

public class TestBase {

    public static ApplicationManager app;

    @BeforeEach
    public void setUp() throws IOException {
        SeleniumLogger.enable();
        SeleniumLogger seleniumLogger = new SeleniumLogger();
        seleniumLogger.setLevel(Level.WARNING);

        if (app == null) {
            var properties = new Properties();
            properties.load(new FileReader(System.getProperty("target", "local.properties")));
            app = new ApplicationManager();
            app.init(System.getProperty("browser", "edge"), properties);
        }
    }

    @AfterEach
    void checkDatabaseConsistency() {
        app.jdbc().checkConsistency();
    }

    public static String randomFile(String dir) {
        var fileNames = new File(dir).list();
        var rnd = new Random();
        var index = rnd.nextInt(fileNames.length);
        return Paths.get(dir, fileNames[index]).toString();
    }
}
