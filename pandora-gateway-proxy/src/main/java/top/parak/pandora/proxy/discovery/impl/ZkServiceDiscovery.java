package top.parak.pandora.proxy.discovery.impl;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.parak.pandora.proxy.config.ConfigContext;
import top.parak.pandora.proxy.discovery.ServiceDiscovery;
import top.parak.pandora.proxy.exception.ServiceDiscoveryException;
import top.parak.pandora.proxy.loadbalance.LoadBalance;
import top.parak.pandora.request.BaseRequest;
import top.parak.pandora.utils.ZKUtils;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * {@link ServiceDiscovery} implementation based on Zookeeper.
 *
 * @author Khighness
 * @since 2023-02-25
 */
public class ZkServiceDiscovery implements ServiceDiscovery {

    private static final Logger LOG = LoggerFactory.getLogger(ZkServiceDiscovery.class);

    private final LoadBalance<String> loadBalance;

    public ZkServiceDiscovery(LoadBalance<String> loadBalance) {
        this.loadBalance = loadBalance;
    }

    @Override
    public InetSocketAddress archiveInstance(BaseRequest request) {
        CuratorFramework zkClient = ZKUtils.getZkClient(ConfigContext.CONFIG.getZookeeperAddress());
        String serviceName = request.getResourceName();
        List<String> serviceInstances = ZKUtils.getChildrenNodes(zkClient, serviceName);

        if (serviceInstances == null || serviceInstances.isEmpty()) {
            throw new ServiceDiscoveryException(serviceName);
        }

        String instance = loadBalance.select(serviceInstances, request);
        LOG.info("[archiveInstance] Service discovery for {}, all: {}, select: {}",
                serviceName, serviceInstances, instance);

        String[] addrArray = instance.split(":");
        return new InetSocketAddress(addrArray[0], Integer.parseInt(addrArray[1]));
    }

}
