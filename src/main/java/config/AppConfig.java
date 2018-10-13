package config;

import sun.applet.Main;

import java.io.IOException;
import java.util.Properties;

public class AppConfig {
    private static final Properties props;

    static {
        props = new Properties();
        try {
            props.load(AppConfig.class.getClassLoader().getResourceAsStream("config/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getTextEncryptionOutputFilePath() {
        return props.getProperty("text.encryption.outputFilePath");
    }

    public static String getPlainTextFilePath() {
        return props.getProperty("plainTextFile.path");
    }

    public static String getEncryptedFilePath() {
        return props.getProperty("encryptedFile.path");
    }

    public static String getDecryptedFilePath() {
        return props.getProperty("decryptedFile.path");
    }

    public static String getDecryptedFilesFromDbPath() {
        return props.getProperty("decryptedFilesFromDB.path");
    }

}
