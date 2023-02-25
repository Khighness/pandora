package top.parak.pandora.server.core;

import org.junit.Before;
import org.junit.Test;

public class NettyWebSocketServerTest {

    private NettyWebSocketServer server;

    @Before
    public void before() {
        server = new NettyWebSocketServer("127.0.0.1", 3333, "ws");
    }

    @Test
    public void start() throws InterruptedException {
        server.start();
    }

}
