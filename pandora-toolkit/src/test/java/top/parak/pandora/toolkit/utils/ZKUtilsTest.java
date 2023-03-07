package top.parak.pandora.toolkit.utils;

import org.apache.curator.framework.CuratorFramework;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZKUtilsTest {

    private static final Logger LOG = LoggerFactory.getLogger(ZKUtilsTest.class);

    private static final String ZK_ADDR = "127.0.0.1:20000";

    private static final String[] ZK_CFG = {"20000", "/tmp/pandora/zk"};

    private final ZooKeeperServerMain zkServer = new ZooKeeperServerMain();

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private CuratorFramework zkClient;

    @Before
    public void before() throws Exception {
        executorService.submit(() -> {
            try {
                ServerConfig serverConfig = new ServerConfig();
                serverConfig.parse(ZK_CFG);
                LOG.error("[before] Succeed to start zookeeper server");
                zkServer.runFromConfig(serverConfig);
            } catch (Exception e) {
                LOG.error("[before] Failed to start zookeeper server: {}", e.getMessage());
            }
        });
        Thread.sleep(1000);
        zkClient = ZKUtils.getZkClient(ZK_ADDR);
    }

    @Test
    public void getZkClient() {
        zkClient.close();
        LOG.info("[getZkClient] OK");
    }

    @Test
    public void createPersistentNode() {
        CuratorFramework zkClient = ZKUtils.getZkClient(ZK_ADDR);
        ZKUtils.createPersistentNode(zkClient, "Khighness");
        LOG.info("[createPersistentNode] OK");
    }

    @Test
    public void getChildrenNodes() {
        final String parentPath = "getChildrenNodes";
        final int total = 10;
        for (int i = 1; i <= total; i++) {
            ZKUtils.createPersistentNode(zkClient, parentPath + "/K*" + i);
            List<String> childrenNodes = ZKUtils.getChildrenNodes(zkClient, parentPath);
            Assert.assertEquals(i, childrenNodes.size());
            LOG.info("[getChildrenNodes] childrenNodes: {}", childrenNodes);
        }
        LOG.info("[getChildrenNodes] OK");
    }

    @Test
    public void clearNodesWithSuffix() {
        final String parentPath = "clearNodesWithSuffifx";
        final int total = 10;
        for (int i = 1; i <= total; i++) {
            ZKUtils.createPersistentNode(zkClient, parentPath + "/K*" + i);
            List<String> childrenNodes = ZKUtils.getChildrenNodes(zkClient, parentPath);
            Assert.assertEquals(i, childrenNodes.size());
        }
        for (int i = 1; i <= total; i++) {
            ZKUtils.clearNodesWithSuffix(zkClient,   "K*" + i);
            List<String> childrenNodes = ZKUtils.getChildrenNodes(zkClient, parentPath);
            Assert.assertEquals(total - i, childrenNodes.size());
        }
        LOG.info("[clearNodesWithSuffix] OK");
    }

}