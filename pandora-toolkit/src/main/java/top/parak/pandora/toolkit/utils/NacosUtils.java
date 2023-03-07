package top.parak.pandora.toolkit.utils;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Nacos API utils.
 * <p>
 * Do not use now.
 * </p>
 *
 * @author Khighness
 * @since 2023-03-07
 */
public class NacosUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ZkUtils.class);

    /**
     * Cache the registered service.
     */
    private static final Set<String> REGISTERED_NAME_SET = ConcurrentHashMap.newKeySet();
    /**
     * Cache the service name and the instances of the service.
     */
    private static final Map<String, List<InetSocketAddress>> SERVICE_INSTANCES_SET = new ConcurrentHashMap<>();
    /**
     * Nacos naming service instance.
     */
    private static volatile NamingService namingService = null;

    private NacosUtils() {
    }

    /**
     * Get the naming service instance of nacos.
     *
     * @param nacosAddress the address of nacos
     * @return the naming service instance
     */
    public static NamingService getNamingService(String nacosAddress) {
        if (namingService == null) {
            synchronized (NacosUtils.class) {
                if (namingService == null) {
                    try {
                        namingService = NamingFactory.createNamingService(nacosAddress);
                    } catch (NacosException e) {
                        throw new RuntimeException("Failed to connect to nacos server", e);
                    }
                }
            }
        }

        return namingService;
    }

    /**
     * Register an instance for service.
     *
     * @param service the naming service
     * @param name    the name of service to be registered
     * @param address the address of service to be registered
     */
    public static void registerInstance(NamingService service, String name,
                                        InetSocketAddress address) {
        try {
            service.registerInstance(name, address.getHostName(), address.getPort(), "pandora");
            REGISTERED_NAME_SET.add(name);
            LOG.info("[registerInstance] Succeed to register instance [{}] for service [{}]", address, name);
        } catch (NacosException e) {
            LOG.error("[registerInstance] Failed to register instance [{}] for service [{}]", address, name, e);
        }
    }

    /**
     * Get the instances of a specified service.
     *
     * @param service the naming service
     * @param name    the name of service to be searched
     * @return the instances
     */
    public static List<InetSocketAddress> getAllInstances(NamingService service, String name) {
        List<InetSocketAddress> result = SERVICE_INSTANCES_SET.get(name);
        if (result != null && !result.isEmpty()) {
            return result;
        }

        try {
            List<Instance> instances = service.getAllInstances(name, "pandora");
            result = Optional.ofNullable(instances).orElse(Collections.emptyList()).stream()
                    .map(instance -> new InetSocketAddress(instance.getIp(), instance.getPort()))
                    .collect(Collectors.toList());
            SERVICE_INSTANCES_SET.put(name, result);
            addListener(service, name);
        } catch (NacosException e) {
            LOG.error("[getAllInstances] Failed to get instances for name: {}", name, e);
        }

        return result;
    }

    /**
     * Deregister an instance for service.
     *
     * @param service the naming service
     * @param name    the name of service to be deregistered
     * @param address the address of service to be deregistered
     */
    public static void deregisterInstance(NamingService service, String name, InetSocketAddress address) {
        try {
            service.deregisterInstance(name, address.getHostName(), address.getPort());
            LOG.info("[deregisterInstance] Succeed to deregister instance [{}] for service [{}]", address, name);
        } catch (NacosException e) {
            LOG.error("[deregisterInstance] Failed to deregister instance [{}] for service [{}]", address, name, e);
        }
    }

    /**
     * Add listener for server.
     *
     * @param service the naming service
     * @param name    the name of service to be listened
     */
    public static void addListener(NamingService service, String name) {
        try {
            service.subscribe(name, event -> {
                NamingEvent namingEvent = (NamingEvent) event;
                String serviceName = namingEvent.getServiceName();
                List<Instance> instances = namingEvent.getInstances();
                List<InetSocketAddress> serviceInstances = instances.stream().map(instance ->
                        new InetSocketAddress(instance.getIp(), instance.getPort())
                ).collect(Collectors.toList());
                SERVICE_INSTANCES_SET.put(serviceName, serviceInstances);
            });
        } catch (NacosException e) {
            LOG.error("[addListener] Failed to subscribe for: {}", name, e);
        }
    }

}
