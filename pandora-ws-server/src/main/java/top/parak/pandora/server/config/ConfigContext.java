package top.parak.pandora.server.config;

import top.parak.pandora.toolkit.utils.PropertiesFileUtils;

import java.util.Properties;

/**
 * Config context.
 *
 * @author Khighness
 * @since 2023-03-07
 */
public class ConfigContext {

    public static final ServerConfig CONFIG;

    static {
        CONFIG = new ServerConfig();
        Properties properties = PropertiesFileUtils.readPropertiesFile("proxy.properties");
        CONFIG.setZookeeperAddress(properties.getProperty("zookeeper.address"));
    }

}
