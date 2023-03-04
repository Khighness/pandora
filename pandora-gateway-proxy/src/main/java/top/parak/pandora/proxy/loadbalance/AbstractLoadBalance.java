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
    public T select(List<T> resources) {
        return null;
    }

}
