package top.parak.pandora.server.registry.impl;

import org.apache.curator.framework.CuratorFramework;
import top.parak.pandora.server.config.ConfigContext;
import top.parak.pandora.server.registry.ServiceRegistry;
import top.parak.pandora.toolkit.utils.ZkUtils;

import java.net.InetSocketAddress;

/**
 * {@link ServiceRegistry} implementation based on Zookeeper.
 *
 * @author Khighness
 * @since 2023-03-04
 */
public class ZkServiceRegistry implements ServiceRegistry {

    @Override
    public boolean registerInstance(String serviceName, InetSocketAddress address) {
        CuratorFramework zkClient = ZkUtils.getZkClient(ConfigContext.CONFIG.getZookeeperAddress());
        ZkUtils.createPersistentNode(zkClient, serviceName + address.toString());
        return true;
    }

}
