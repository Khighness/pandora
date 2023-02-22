package top.parak.pandora.client.core;

import org.junit.Before;
import org.junit.Test;
import top.parak.pandora.client.exception.WebSocketClientException;
import top.parak.pandora.client.factory.WebSocketClientFactory;

public class NettyWebSocketClientTest {

    private WebSocketClient nettyWebSocketClient;

    @Before
    public void before() {
        nettyWebSocketClient = WebSocketClientFactory.create("ws://127.0.0.1:3333/websocket/TestNettyWebSocketClient");
    }

    @Test
    public void open() throws Exception {
        nettyWebSocketClient.open();
    }

    @Test
    public void close() throws Exception {
        try {
            nettyWebSocketClient.open();
        } finally {
            nettyWebSocketClient.close();
        }
    }

    @Test
    public void send() throws Exception {
        try {
            nettyWebSocketClient.open();
            for (int i = 1; i <= 10; i++) {
                nettyWebSocketClient.send("Hello * " + i);
            }
        } finally {
            nettyWebSocketClient.close();
        }
    }

    @Test
    public void reopen() throws Exception {
        nettyWebSocketClient.open();
        nettyWebSocketClient.close();
        nettyWebSocketClient.open();
    }

    @Test(expected = WebSocketClientException.class)
    public void closeWithoutOpen() throws Exception {
        nettyWebSocketClient.close();
    }

    @Test(expected = WebSocketClientException.class)
    public void sendBeforOpen() throws Exception {
        nettyWebSocketClient.send("Hello");
    }

}