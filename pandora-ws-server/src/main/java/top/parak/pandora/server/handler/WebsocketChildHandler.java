package top.parak.pandora.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import top.parak.pandora.common.HttpConstants;
import top.parak.pandora.server.logic.SessionManageService;

import java.util.concurrent.TimeUnit;

/**
 * Server handler initializer.
 *
 * @author cantai
 * @since 2023-02-24
 */
public class WebsocketChildHandler extends ChannelInitializer<SocketChannel> {

    private final String uri;

    private final SessionManageService sessionManageService;

    public WebsocketChildHandler(String uri, SessionManageService sessionManageService) {
        this.uri = uri;
        this.sessionManageService = sessionManageService;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("http-codec", new HttpServerCodec());
        pipeline.addLast("aggregator", new HttpObjectAggregator(HttpConstants.MAX_CONTENT_LENGTH));
        pipeline.addLast("idea-handler", new IdleStateHandler(10, 10, 120, TimeUnit.MINUTES));
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());
        pipeline.addLast("heartbeater", new HeartbeatHandler());
        pipeline.addLast("logic-handler", new WebSocketServerHandler(uri, sessionManageService));
    }

}
