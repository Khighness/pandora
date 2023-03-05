package top.parak.pandora.proxy.loadbalance;

import java.util.List;

/**
 * Load balance.
 *
 * @author Khighness
 * @since 2023-03-04
 */
public interface LoadBalance<T> {

    /**
     * Select one of the resources.
     *
     * @param resourceList the resource list
     * @param resourceName the resource name
     * @return the resource selected by load balance algorithm
     */
    T select(List<T> resourceList, String resourceName);

}
