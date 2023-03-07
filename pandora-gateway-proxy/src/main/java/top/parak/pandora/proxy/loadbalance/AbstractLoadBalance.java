package top.parak.pandora.proxy.loadbalance;

import top.parak.pandora.toolkit.request.BaseRequest;

import java.util.List;

/**
 * Abstract load balance.
 *
 * @author Khighness
 * @since 2023-03-04
 */
public abstract class AbstractLoadBalance<T> implements LoadBalance<T> {

    @Override
    public T select(List<T> resourceList, BaseRequest request) {
        if (resourceList == null || resourceList.isEmpty()) {
            return null;
        }
        if (resourceList.size() == 1) {
            return resourceList.get(0);
        }
        return doSelect(resourceList, request);
    }

    /**
     * Do select by a specific algorithm.
     *
     * @param resources the resource list
     * @param request   the resource request
     * @return the resource selected by load balance algorithm
     */
    protected abstract T doSelect(List<T> resources, BaseRequest request);

}
