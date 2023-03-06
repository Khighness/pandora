package top.parak.pandora.proxy.discovery;

import top.parak.pandora.request.BaseRequest;

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
     * @param request the service discovery request
     * @return the unique address of service instance
     */
    InetSocketAddress archiveInstance(BaseRequest request);

}
