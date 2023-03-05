package top.parak.pandora.proxy.loadbalance.impl;

import top.parak.pandora.proxy.loadbalance.AbstractLoadBalance;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Consistent hash load balancing algorithm.
 *
 * @author Khighness
 * @since 2023-03-05
 */
public class ConsistentHashLoadBalance<T> extends AbstractLoadBalance<T> {

    private final Map<String, ConsistentHashSelector<T>> selectors = new ConcurrentHashMap<>();

    @Override
    protected T doSelect(List<T> resourceList, String resourceName) {
        int identityHashCode = System.identityHashCode(resourceList);
        ConsistentHashSelector<T> selector = selectors.get(resourceName);
        if (selector == null || selector.identityHashCode != identityHashCode) {
            selector = new ConsistentHashSelector<T>(resourceList, 160, identityHashCode);
            selectors.put(resourceName, selector);
        }
        return selector.select(resourceName + System.nanoTime());
    }

    static class ConsistentHashSelector<T> {
        private final TreeMap<Long, T> virtualInvokers;

        private final int identityHashCode;

        ConsistentHashSelector(List<T> invokers, int replicaNumber, int identityHashCode) {
            this.virtualInvokers = new TreeMap<>();
            this.identityHashCode = identityHashCode;

            for (T invoker : invokers) {
                for (int i = 0; i < replicaNumber >> 2; i++) {
                    byte[] digest = md5(invoker.toString() + i);
                    for (int h = 0; h < 4; h++) {
                        long hash = hash(digest, h);
                        virtualInvokers.put(hash, invoker);
                    }
                }
            }
        }

        static byte[] md5(String key) {
            MessageDigest md;

            try {
                md = MessageDigest.getInstance("MD5");
                byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
                md.update(bytes);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }

            return md.digest();
        }

        static long hash(byte[] digest, int idx) {
            return ((long) (digest[3 + idx * 4] & 255) << 24
                    | (long) (digest[2 + idx * 4] & 255) << 16
                    | (long) (digest[1 + idx * 4] & 255) << 8
                    | (long) (digest[idx * 4] & 255))
                    & 4294967295L;
        }

        public T select(String key) {
            byte[] digest = md5(key);
            return selectForKey(hash(digest, 0));
        }

        public T selectForKey(long hashCode) {
            Map.Entry<Long, T> entry = virtualInvokers.tailMap(hashCode, true).firstEntry();

            if (entry == null) {
                entry = virtualInvokers.firstEntry();
            }

            return entry.getValue();
        }
    }

}
