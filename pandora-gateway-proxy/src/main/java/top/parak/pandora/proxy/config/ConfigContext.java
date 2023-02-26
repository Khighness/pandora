package top.parak.pandora.proxy.config;

import top.parak.pandora.utils.PropertiesFileUtils;

import java.util.Properties;

/**
 * Config context.
 *
 * @author cantai
 * @since 2023-02-26
 */
public class ConfigContext {

    public static final ProxyConfig CONFIG;

    static {
        CONFIG = new ProxyConfig();
        Properties properties = PropertiesFileUtils.readPropertiesFile("proxy.properties");
        CONFIG.setZookeeperAddress(properties.getProperty("zookeeper.address"));
    }

}
