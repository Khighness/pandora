package top.parak.pandora.proxy.discovery.impl;

import top.parak.pandora.proxy.discovery.ServiceDiscovery;

import java.net.InetSocketAddress;

/**
 * {@link ServiceDiscovery} implementation based on Zookeeper.
 *
 * @author Khighness
 * @since 2023-02-25
 */
public class ZKServiceDiscoveryImpl implements ServiceDiscovery {

    @Override
    public InetSocketAddress archiveInstance(String serviceName) {
        return null;
    }

}
