package top.parak.pandora.client.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
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

import java.net.URI;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * {@link WebSocketClient} implementation on Netty.
 *
 * @author Khighness
 * @since 2023-02-22
 */
@ChannelHandler.Sharable
public class NettyWebSocketClient implements WebSocketClient {

    private static final int STATE_INITIAL = 0;
    private static final int STATE_OPENED  = 1;
    private static final int STATE_CLOSED  = 2;

    /**
     * The state of client.
     */
    private volatile int state;
    private static final AtomicIntegerFieldUpdater<NettyWebSocketClient> STATE_UPDATER
            = AtomicIntegerFieldUpdater.newUpdater(NettyWebSocketClient.class, "state");

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
     * Create a {@link NettyWebSocketClient}.
     *
     * @param uri the uri to connect the websocket server.
     */
    public NettyWebSocketClient(URI uri) {
        this.state = STATE_INITIAL;
        this.uri = uri;
    }

    @Override
    public void open() throws Exception {
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
                        // 将请求与应答消息进行http编解码
                        pipeline.addLast("http-codec", new HttpClientCodec());
                        // 将http消息的多个部分组合成一条完成的http消息
                        pipeline.addLast("aggregator", new HttpObjectAggregator(HttpConstants.MAX_CONTENT_LENGTH));
                        // 自定义
                        pipeline.addLast("client-handler", webSocketClientHandler);
                    }
                });

        ChannelFuture future = bootstrap.connect(uri.getHost(), uri.getPort()).sync();
        webSocketClientHandler.handshakeFuture().sync();

        this.ctx = future.channel().pipeline().lastContext();
        STATE_UPDATER.compareAndSet(this, state, STATE_OPENED);
    }

    @Override
    public void close() throws InterruptedException {
        if (state == STATE_INITIAL || state == STATE_CLOSED) {
            throw new WebSocketClientException("Websocket client is not opened or already closed");
        }

        ctx.writeAndFlush(new CloseWebSocketFrame());
        ctx.close().sync();
        loopGroup.shutdownGracefully();
        STATE_UPDATER.compareAndSet(this, state, STATE_CLOSED);
    }

    @Override
    public void send(String text) throws WebSocketClientException {
        if (state == STATE_INITIAL || state == STATE_CLOSED) {
            throw new WebSocketClientException("Websocket client is not opened or already closed");
        }

        ctx.writeAndFlush(new TextWebSocketFrame(text));
    }

}
