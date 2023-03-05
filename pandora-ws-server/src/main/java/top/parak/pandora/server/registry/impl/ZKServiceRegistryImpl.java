package top.parak.pandora.server.registry.impl;

import top.parak.pandora.server.registry.ServiceRegistry;

import java.net.InetSocketAddress;

/**
 * {@link ServiceRegistry} implementation based on Zookeeper.
 *
 * @author Khighness
 * @since 2023-03-04
 */
public class ZKServiceRegistryImpl implements ServiceRegistry {

    @Override
    public boolean registerInstance(String serviceName, InetSocketAddress address) {
        return false;
    }

}
