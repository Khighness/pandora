package top.parak.pandora.proxy.utils;

import org.apache.curator.framework.CuratorFramework;

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


    private static final int BASE_SLEEP_TIME = 1000;

    private static final int MAX_RETRIES = 3;

    private static final String ZK_REGISTER_ROOT_PATH = "/svr-dvr";

    private static final Set<String> REGISTER_PATH_SET = new HashSet<>();

    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();

    private static CuratorFramework zkClient;

    private ZKUtils() {
    }




}
