package top.parak.pandora.proxy.discovery;

import java.net.InetSocketAddress;

/**
 * Service Discovery.
 *
 * @author Khighness
 * @since 2023-02-25
 */
public interface ServiceDiscovery {

    /**
     * Archive the address of service instance by service name.
     *
     * @param serviceName the name of service
     * @return the unique address of service instance
     */
    InetSocketAddress archiveInstance(String serviceName);

}
