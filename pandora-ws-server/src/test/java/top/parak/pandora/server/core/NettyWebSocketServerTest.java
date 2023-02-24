package top.parak.pandora.server.core;

import org.junit.Before;
import org.junit.Test;
import top.parak.pandora.server.model.Session;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class NettyWebSocketServerTest {

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    private NettyWebSocketServer server;

    @Before
    public void before() {
        server = new NettyWebSocketServer("127.0.0.1", 3333, "ws");
    }

    @Test
    public void start() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        server.start();
    }

}
