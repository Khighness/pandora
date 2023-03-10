package top.parak.pandora.server.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.parak.pandora.server.exception.WebSocketServerException;
import top.parak.pandora.server.handler.WebSocketServerHandler;
import top.parak.pandora.server.handler.WebsocketServerInitializer;
import top.parak.pandora.server.logic.SessionManageService;
import top.parak.pandora.server.logic.impl.MemorySessionManageService;
import top.parak.pandora.server.model.Session;
import top.parak.pandora.toolkit.statemachine.ReOpenableStatemachine;

import java.net.URI;

/**
 * {@link WebSocketServer} implementation on Netty.
 *
 * @author Khighness
 * @since 2023-02-23
 */
public class NettyWebSocketServer extends ReOpenableStatemachine implements WebSocketServer {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServerHandler.class);

    /**
     * The uri of websocket server.
     */
    private final URI uri;

    /**
     * The event loop groups.
     */
    private EventLoopGroup bossGroup, workerGroup;

    /**
     * Client session manager.
     */
    private SessionManageService sessionManageService;

    /**
     * Create an instance.
     *
     * @param host the host of server
     * @param port the port of server
     * @param contextPath the context path of server
     */
    public NettyWebSocketServer(String host, int port, String contextPath) {
        this.uri = URI.create(String.format("ws://%s:%d/%s", host, port, contextPath));
        this.sessionManageService = new MemorySessionManageService();
    }

    @Override
    public void start() throws WebSocketServerException {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
            WebsocketServerInitializer serverInitializer =
                    new WebsocketServerInitializer(uri.toString(), sessionManageService);

            serverBootstrap
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(serverInitializer);

            Channel channel = serverBootstrap.bind(uri.getHost(), uri.getPort()).channel();
            transferToOpened();
            LOG.info("Netty websocket server started on [{}]", uri);

            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            throw new WebSocketServerException("Websocket server is interrupted");
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void send(Session session, String message) throws WebSocketServerException {
        LOG.info("[send] {}: {}", session, message);
        if (!isOpened()) {
            throw new WebSocketServerException("Websocket server is not opened or already closed");
        }

        sessionManageService.publishMessage(session.getId(), message);
    }

}
