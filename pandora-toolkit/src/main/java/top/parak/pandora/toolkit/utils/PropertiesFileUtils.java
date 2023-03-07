package top.parak.pandora.toolkit.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Properties file utils.
 *
 * @author Khighness
 * @since 2022-09-11
 */
public final class PropertiesFileUtils {
    private static final Logger LOG = LoggerFactory.getLogger(PropertiesFileUtils.class);

    private PropertiesFileUtils() {
    }

    /**
     * Read properties file in classpath.
     *
     * @param fileName the name of file
     * @return a {@link Properties} instance
     */
    public static Properties readPropertiesFile(String fileName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        String configPath = "";
        if (url != null) {
            configPath = url.getPath() + fileName;
        }
        Properties properties = null;
        try (InputStreamReader inputStreamReader = new InputStreamReader(
                new FileInputStream(configPath), StandardCharsets.UTF_8)) {
            properties = new Properties();
            properties.load(inputStreamReader);
        } catch (IOException e) {
            LOG.error("[readPropertiesFile] occur exception: {}", e.getMessage(), e);
        }
        return properties;
    }

}
