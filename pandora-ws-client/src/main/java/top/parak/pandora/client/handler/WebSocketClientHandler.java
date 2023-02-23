package top.parak.pandora.client.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logic handler for {@link top.parak.pandora.client.core.NettyWebSocketClient}.
 *
 * @author Khighness
 * @since 2023-02-22
 */
public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketClientHandler.class);

    private final WebSocketClientHandshaker webSocketClientHandshaker;

    private ChannelPromise handshakeFuture;

    public WebSocketClientHandler(WebSocketClientHandshaker webSocketClientHandshaker) {
        this.webSocketClientHandshaker = webSocketClientHandshaker;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();

        if (!webSocketClientHandshaker.isHandshakeComplete()) {
            try {
                webSocketClientHandshaker.finishHandshake(channel, (FullHttpResponse) msg);
                handshakeFuture.setSuccess();
                LOG.info("|==================== [握手成功: {}] ====================|", channel.remoteAddress());
            } catch (WebSocketHandshakeException e) {
                LOG.info("|==================== [握手失败: {}] ====================|", channel.remoteAddress(), e);
                handshakeFuture.setFailure(e);
            }
            return;
        }
        ctx.flush();

        if (msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame frame = (TextWebSocketFrame) msg;
            LOG.info("Server: {}", frame.text());
        } else if (msg instanceof PongWebSocketFrame) {
            LOG.info("Receive pong frame from server");
        } else if (msg instanceof CloseWebSocketFrame) {
            LOG.info("Receive close frame from server");
            channel.close();
            ctx.close();
            LOG.info("|==================== [断开连接: {}] ====================|", channel.remoteAddress());
        } else if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + response.status() +
                    ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        LOG.info("|==================== [建立连接: {}] ====================|", channel.remoteAddress());
        webSocketClientHandshaker.handshake(channel);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        LOG.info("|==================== [断开连接: {}] ====================|", channel.remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.info("|==================== [连接异常: {}] ====================|", cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

}
