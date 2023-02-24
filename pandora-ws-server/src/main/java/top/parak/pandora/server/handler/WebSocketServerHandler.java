package top.parak.pandora.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.parak.pandora.server.frame.FrameStrategyContext;
import top.parak.pandora.server.logic.FirstHandshakeService;
import top.parak.pandora.server.logic.SessionManageService;
import top.parak.pandora.server.logic.impl.DefaultFirstHandshakeService;
import top.parak.pandora.server.model.Session;

/**
 * Logic handler for {@link top.parak.pandora.server.core.NettyWebSocketServer}.
 *
 * @author Khighness
 * @since 2023-02-23
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger LOG = LoggerFactory.getLogger(WebSocketServerHandler.class);

    private final String uri;

    private final FirstHandshakeService firstHandshakeService;

    private final SessionManageService sessionManageService;

    public WebSocketServerHandler(String uri, SessionManageService sessionManageService) {
        this.uri = uri;
        this.firstHandshakeService = new DefaultFirstHandshakeService();
        this.sessionManageService = sessionManageService;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest req = (FullHttpRequest) msg;
            LOG.info("|==================== [请求握手: {}] ====================|", ctx.channel().remoteAddress());
            handleHttpHandShake(ctx, req);
        } else if (msg instanceof WebSocketFrame) {
            WebSocketFrame frame = (WebSocketFrame) msg;
            handleWebsocketFrame(ctx, frame);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("|==================== [建立连接: {}] ====================|", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        closeConnection(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.info("|==================== [连接异常: {}], {}|", ctx.channel().remoteAddress(), cause.getMessage());
        closeConnection(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }


    /**
     * Handle the handshake request.
     * <p>
     * Websocket client sends the handshake request based on http.
     * </p>
     *
     * @param ctx channel handler context
     * @param req handshake request from client
     */
    private void handleHttpHandShake(ChannelHandlerContext ctx, FullHttpRequest req) {
        Channel channel = ctx.channel();
        Session session = firstHandshakeService.extractSession(req.uri(), channel.remoteAddress());

        WebSocketServerHandshakerFactory handshakerFactory = new WebSocketServerHandshakerFactory(uri,
                null, Boolean.FALSE);
        WebSocketServerHandshaker handshaker = handshakerFactory.newHandshaker(req);

        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(channel);
            LOG.warn("[handleHttpHandShake] session: {}, unsupported websocket version: {}", session, req.protocolVersion());
        } else {
            handshaker.handshake(channel, req);
            sessionManageService.saveSession(session, channel);
            LOG.info("[handleHttpHandShake] save session: {}", session);
        }
    }

    /**
     * Handle the websocket frame,
     *
     * @param ctx   channel handler context
     * @param frame websocket frame
     */
    private void handleWebsocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof CloseWebSocketFrame) {
            closeConnection(ctx);
            return;
        }

        FrameStrategyContext.handleWebsocketFrame(ctx.channel(), frame);
    }

    /**
     * Close connection.
     *
     * @param ctx channel handler context
     */
    private void closeConnection(ChannelHandlerContext ctx) {
        ctx.close();
        Session session = sessionManageService.removeSession(ctx.channel().remoteAddress());
        if (session != null) {
            LOG.info("[closeConnection] succeed ro remove session: {}", session);
            LOG.info("|==================== [断开连接: {}] ====================|", ctx.channel().remoteAddress());
        }
    }

}
