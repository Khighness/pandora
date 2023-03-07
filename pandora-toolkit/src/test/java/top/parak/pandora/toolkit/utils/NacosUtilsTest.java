package top.parak.pandora.toolkit.utils;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

public class NacosUtilsTest {

    private static final Logger LOG = LoggerFactory.getLogger(NacosUtilsTest.class);

    private static final String NACOS_ADDR = "127.0.0.1:8848";

    // Need to be created in nacos console manually.
    private static final String TEST_SERVICE_NAME = "pandora.test";

    private NamingService namingService;

    @Before // Must start the nacos server.
    public void setup() {
        namingService = NacosUtils.getNamingService(NACOS_ADDR);
    }

    @Test
    @Ignore
    public void getNamingService() throws NacosException {
        LOG.info("[getNamingService] OK");
    }

    @Test
    @Ignore
    public void registerInstance() {
        NacosUtils.registerInstance(namingService, TEST_SERVICE_NAME,
                new InetSocketAddress("192.168.0.1", 3333));
        LOG.info("[registerInstance] OK");
    }

    @Test
    @Ignore
    public void getAllInstances() {
        InetSocketAddress address = new InetSocketAddress("192.168.1.1", 3333);
        NacosUtils.registerInstance(namingService, TEST_SERVICE_NAME, address);
        List<InetSocketAddress> instances = NacosUtils.getAllInstances(namingService, TEST_SERVICE_NAME);
        LOG.info("[getAllInstances] OK");
    }

    @Test
    @Ignore
    public void deregisterInstance() {
    }

}