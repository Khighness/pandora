package top.parak.pandora.proxy.loadbalance.impl;

import top.parak.pandora.proxy.loadbalance.AbstractLoadBalance;

import java.util.List;
import java.util.Random;

/**
 * Random load balancing algorithm.
 *
 * @author Khighness
 * @since 2023-03-04
 */
public class RandomLoadBalance<T> extends AbstractLoadBalance<T> {

    @Override
    protected T doSelect(List<T> resourceList, String resourceName) {
        Random random = new Random();
        return resourceList.get(random.nextInt(resourceList.size()));
    }

}
