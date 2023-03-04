package top.parak.pandora.server.registry;

import java.net.InetSocketAddress;

/**
 * Service Registry.
 *
 * @author Khighness
 * @since 2023-03-04
 */
public interface ServiceRegistry {

    /**
     * Register the service instance by address to registry center.
     *
     * @param serviceName the name of service
     * @param address the unique address of service instance
     * @return if the service instance is registered successfully
     */
    boolean registerInstance(String serviceName, InetSocketAddress address);

}
