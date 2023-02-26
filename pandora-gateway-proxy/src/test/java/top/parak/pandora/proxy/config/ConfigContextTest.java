package top.parak.pandora.proxy.config;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigContextTest {

    @Test
    public void getZookeeperAddress() {
        System.out.println(ConfigContext.CONFIG.getZookeeperAddress());
    }

}