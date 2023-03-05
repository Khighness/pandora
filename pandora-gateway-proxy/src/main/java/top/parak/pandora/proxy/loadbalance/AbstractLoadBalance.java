package top.parak.pandora.proxy.loadbalance;

import java.util.List;

/**
 * Abstract load balance.
 *
 * @author Khighness
 * @since 2023-03-04
 */
public abstract class AbstractLoadBalance<T> implements LoadBalance<T> {

    @Override
    public T select(List<T> resourceList, String resourceName) {
        if (resourceList == null || resourceList.isEmpty()) {
            return null;
        }
        if (resourceList.size() == 1) {
            return resourceList.get(0);
        }
        return doSelect(resourceList, resourceName);
    }

    /**
     * Do select by a specific algorithm.
     *
     * @param resourceList the resource list
     * @param resourceName the resource name
     * @return the resource selected by load balance algorithm
     */
    protected abstract T doSelect(List<T> resourceList, String resourceName);

}
