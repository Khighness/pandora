package top.parak.pandora.utils;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Zookeeper client utils.
 *
 * @author Khighness
 * @since 2023-02-25
 */
public class ZKUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ZKUtils.class);

    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;
    private static final String ZK_REGISTER_ROOT_PATH = "/svr-dvr";

    private static final Set<String> REGISTER_PATH_SET = new HashSet<>();
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();

    private static CuratorFramework zkClient;

    private ZKUtils() {
    }

    /**
     * Create persistent nodes.
     *
     * @param zkClient Zookeeper client
     * @param path     the path to be created
     */
    public static void createPersistentNode(CuratorFramework zkClient, String path) {
        try {
            if (REGISTER_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                LOG.info("[createPersistentNode] The node already exists: {}", path);
            } else {
                zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
            }
        } catch (Exception e) {
            LOG.error("[createPersistentNode] Failed to create node: {}, error: {}", path, e.getMessage(), e);
        }
    }

    private static List<String> getChildrenNodes(CuratorFramework zkClient, String path) {
        if (SERVICE_ADDRESS_MAP.containsKey(path)) {

        }
        return null;
    }

}
