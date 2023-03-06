package top.parak.pandora.proxy.loadbalance.impl;

import top.parak.pandora.proxy.loadbalance.AbstractLoadBalance;
import top.parak.pandora.proxy.loadbalance.ResourceRequest;

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
    protected T doSelect(List<T> resources, ResourceRequest ignored) {
        Random random = new Random();
        return resources.get(random.nextInt(resources.size()));
    }

}
