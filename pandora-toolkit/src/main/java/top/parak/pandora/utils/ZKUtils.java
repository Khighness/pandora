package top.parak.pandora.utils;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.listen.StandardListenerManager;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Zookeeper API utils.
 *
 * @author Khighness
 * @since 2023-02-25
 */
public class ZKUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ZKUtils.class);

    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;
    private static final String ZK_REGISTER_ROOT_PATH = "/pandora";

    /**
     * Cache the registered path.
     */
    private static final Set<String> REGISTERED_PATH_SET = new HashSet<>();
    /**
     * Cache the node path and the children of node.
     */
    private static final Map<String, List<String>> PATH_CHILDREN_MAP = new ConcurrentHashMap<>();
    /**
     * Zookeeper client instance.
     */
    private static CuratorFramework zkClient;

    private ZKUtils() {
    }

    /**
     * Get the Zookeeper client instance.
     *
     * @param zkAddress the address of zookeeper
     * @return the Zookeeper client instance
     */
    public static CuratorFramework getZkClient(String zkAddress) {
        if (zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            return zkClient;
        }

        synchronized (ZKUtils.class) {
            ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
            zkClient = CuratorFrameworkFactory.builder()
                    .connectString(zkAddress)
                    .retryPolicy(retryPolicy)
                    .build();
            zkClient.start();
            LOG.info("Zookeeper client is initialized successfully");

            try {
                if (!zkClient.blockUntilConnected(30, TimeUnit.SECONDS)) {
                    throw new RuntimeException("Timeout waiting to connect to zookeeper");
                }
            } catch (InterruptedException e) {
                LOG.error("[getZkClient] sleep interrupted");
            }
        }
        return zkClient;
    }

    /**
     * Create a persistent node.
     *
     * @param zkClient Zookeeper client
     * @param path     the path of node to be created
     */
    public static void createPersistentNode(CuratorFramework zkClient, String path) {
        path = ZK_REGISTER_ROOT_PATH + "/" + path;
        try {
            if (REGISTERED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                LOG.info("[createPersistentNode] The node already exists: {}", path);
            } else {
                zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                LOG.info("[createPersistentNode] Succeed to create node: {}", path);
                REGISTERED_PATH_SET.add(path);
            }
        } catch (Exception e) {
            LOG.error("[createPersistentNode] Failed to create node: {}, error: {}", path, e.getMessage(), e);
        }
    }

    /**
     * Get the children of node corresponding to the path.
     *
     * @param zkClient Zookeeper client
     * @param path     the path of node
     * @return the children of node
     */
    public static List<String> getChildrenNodes(CuratorFramework zkClient, String path) {
        List<String> addresses = PATH_CHILDREN_MAP.get(path);
        if (addresses != null && addresses.size() > 0) {
            return PATH_CHILDREN_MAP.get(path);
        }

        List<String> result = null;
        path = ZK_REGISTER_ROOT_PATH + "/" + path;
        try {
            result = zkClient.getChildren().forPath(path);
            PATH_CHILDREN_MAP.put(path, result);
            registerWatcher(zkClient, path);
        } catch (Exception e) {
            LOG.error("[getChildrenNodes] Failed to get node: {}", path);
        }

        return result;
    }

    /**
     * Clear the nodes corresponding to the suffix path.
     *
     * @param zkClient   Zookeeper client
     * @param suffixPath the suffix of the path
     */
    public static void clearNodesWithSuffix(CuratorFramework zkClient, String suffixPath) {
        REGISTERED_PATH_SET.stream().parallel().forEach(path -> {
            try {
                if (path.endsWith(suffixPath)) {
                    zkClient.delete().forPath(path);
                }
            } catch (Exception e) {
                LOG.error("[clearNodesWithSuffifx] Failed to delete node: {}", path);
            }
        });
        LOG.info("[clearNodesWithSuffifx] All nodes whose suffix matches ({}) are cleared", suffixPath);
    }

    /**
     * Register watcher for the given path.
     *
     * @param zkClient Zookeeper client
     * @param path     the path of node
     * @throws Exception if chach fails to start
     */
    public static void registerWatcher(CuratorFramework zkClient, String path) throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, path, true);
        PathChildrenCacheListener listener = (zkCli, cacheEvent) -> {
            LOG.info("[registerWatcher] path: {}, event: {}", path, cacheEvent);
            List<String> children = zkCli.getChildren().forPath(path);
            PATH_CHILDREN_MAP.put(path, children);
        };
        StandardListenerManager.standard().addListener(listener);
        pathChildrenCache.start();
    }

}
