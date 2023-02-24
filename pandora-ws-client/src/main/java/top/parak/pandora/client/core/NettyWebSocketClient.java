package top.parak.pandora.client.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import top.parak.pandora.client.exception.WebSocketClientException;
import top.parak.pandora.client.handler.WebSocketClientHandler;
import top.parak.pandora.common.HttpConstants;
import top.parak.pandora.statemachine.ReOpenableStatemachine;

import java.net.URI;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {@link WebSocketClient} implementation on Netty.
 *
 * @author Khighness
 * @since 2023-02-22
 */
public class NettyWebSocketClient extends ReOpenableStatemachine implements WebSocketClient {

    private final ReentrantLock lock;

    /**
     * The uri to connect the websocket server.
     */
    private final URI uri;

    /**
     * The channel to interact with server.
     */
    private ChannelHandlerContext ctx;

    /**
     * The event loop group.
     */
    private EventLoopGroup loopGroup;

    /**
     * Create an instance.
     *
     * @param uri the uri to connect the websocket server.
     */
    public NettyWebSocketClient(URI uri) {
        this.lock = new ReentrantLock();
        this.uri = uri;
    }

    @Override
    public void open() throws Exception {
        lock.lock();
        try {
            Bootstrap bootstrap = new Bootstrap();
            loopGroup = new NioEventLoopGroup();
            bootstrap.group(loopGroup);

            WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory
                    .newHandshaker(uri, WebSocketVersion.V13, null, false, new DefaultHttpHeaders());
            WebSocketClientHandler webSocketClientHandler = new WebSocketClientHandler(handshaker);

            bootstrap.channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ChannelPipeline pipeline = sc.pipeline();
                            pipeline.addLast("http-codec", new HttpClientCodec());
                            pipeline.addLast("aggregator", new HttpObjectAggregator(HttpConstants.MAX_CONTENT_LENGTH));
                            pipeline.addLast("client-handler", webSocketClientHandler);
                        }
                    });

            ChannelFuture future = bootstrap.connect(uri.getHost(), uri.getPort()).sync();
            webSocketClientHandler.handshakeFuture().sync();

            this.ctx = future.channel().pipeline().lastContext();
            transferToOpened();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void close() throws InterruptedException {
        if (!isOpened()) {
            throw new WebSocketClientException("Websocket client is not opened or already closed");
        }

        lock.lock();
        try {
            ctx.writeAndFlush(new CloseWebSocketFrame());
            ctx.close().sync();
            loopGroup.shutdownGracefully();
            transferToClosed();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void send(String text) throws WebSocketClientException {
        if (!isOpened()) {
            throw new WebSocketClientException("Websocket client is not opened or already closed");
        }

        ctx.writeAndFlush(new TextWebSocketFrame(text));
    }

}
