package top.parak.pandora.server.model;

import java.io.Serializable;

/**
 * Websocket session DTO.
 *
 * @author cantai
 * @since 2023-02-24
 */
public class Session implements Serializable {
    private static final long serialVersionUID = -1191565010907111186L;

    /**
     * The unique key in uri of client.
     */
    private final String id;

    /**
     * The remote address of client.
     */
    private String cliAddr;

    public Session(String id, String cliAddr) {
        this.id = id;
        this.cliAddr = cliAddr;
    }

    public String getId() {
        return id;
    }

    public String getCliAddr() {
        return cliAddr;
    }

    public void setCliAddr(String cliAddr) {
        this.cliAddr = cliAddr;
    }

    @Override
    public String toString() {
        return id + ":" + cliAddr;
    }

}
