package top.parak.pandora.server.logic;

import io.netty.channel.Channel;
import top.parak.pandora.server.model.Session;

import java.net.SocketAddress;

/**
 * Session manege service.
 *
 * @author Khighness
 * @since 2023-02-24
 */
public interface SessionManageService {

    /**
     * Save session.
     *
     * @param session websocket session
     * @param channel I/O channel to client
     * @return true if save successfully, otherwise false
     */
    boolean saveSession(Session session, Channel channel);

    /**
     * Get session corresponding to the given client address.
     *
     * @param cliAddr the address of client
     * @return the removed session, {@code null} if the session does not exist.
     */
    Session getByAddr(SocketAddress cliAddr);

    /**
     * Get session corresponding to the given client address.
     *
     * @param cliAddr the address of client
     * @return the removed session, {@code null} if the session does not exist.
     */
    Session getByAddr(String cliAddr);

    /**
     * Remove session corresponding to the given client address.
     *
     * @param cliAddr the address of client
     * @return the removed session, {@code null} if the session does not exist.
     */
    Session removeSession(SocketAddress cliAddr);

    /**
     * Remove session corresponding to the given client address.
     *
     * @param cliAddr client address.
     * @return the removed session, {@code null} if the session does not exist.
     */
    Session removeSession(String cliAddr);

    /**
     * Publish message to the client to the given client id.
     *
     * @param id      the id of client
     * @param message the message to be sent
     * @return true if success, otherwise false
     */
    boolean publishMessage(String id, String message);

}
