package top.parak.pandora.server.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NettyWebSocketServerTest {

    private NettyWebSocketServer server;

    @Before
    public void before() {
        server = new NettyWebSocketServer("127.0.0.1", 13333, "/ws");
    }

    @Test
    public void startAndStop() throws InterruptedException {
        server.start();
        Thread.sleep(100);
        server.stop();
    }

}