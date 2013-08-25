package us.shareby.core.notification;

import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.SocketClient;
import org.slf4j.Logger;

/**
 * @author chengdong
 */
public class ClientPrintCommandListener implements ProtocolCommandListener {

    private final Logger __logger;
    private final boolean __nologin;
    private final char __eolMarker;
    private final boolean __directionMarker;

    /**
     * Create the default instance which prints everything.
     *
     * @param logger where to writer the commands and responses
     */
    public ClientPrintCommandListener(Logger logger) {
        this(logger, false);
    }

    /**
     * Create an instance which optionally suppresses login command text.
     *
     * @param logger        where to write the commands and responses
     * @param suppressLogin if {@code true}, only print command name for login
     * @since 3.0
     */
    public ClientPrintCommandListener(Logger logger, boolean suppressLogin) {
        this(logger, suppressLogin, (char) 0);
    }

    /**
     * Create an instance which optionally suppresses login command text and
     * indicates where the EOL starts with the specified character.
     *
     * @param logger        where to write the commands and responses
     * @param suppressLogin if {@code true}, only print command name for login
     * @param eolMarker     if non-zero, add a marker just before the EOL.
     * @since 3.0
     */
    public ClientPrintCommandListener(Logger logger, boolean suppressLogin,
                                      char eolMarker) {
        this(logger, suppressLogin, eolMarker, false);
    }

    /**
     * Create an instance which optionally suppresses login command text
     * and indicates where the EOL starts with the specified character.
     *
     * @param logger        where to write the commands and responses
     * @param suppressLogin if {@code true}, only print command name for login
     * @param eolMarker     if non-zero, add a marker just before the EOL.
     * @param showDirection if {@code true}, add "> " or "< " as appropriate to the output
     * @since 3.0
     */
    public ClientPrintCommandListener(Logger logger, boolean suppressLogin,
                                      char eolMarker, boolean showDirection) {
        __logger = logger;
        __nologin = suppressLogin;
        __eolMarker = eolMarker;
        __directionMarker = showDirection;
    }

    @Override
    public void protocolCommandSent(ProtocolCommandEvent event) {

        StringBuilder __writer = new StringBuilder();

        if (__directionMarker) {
            __writer.append(">");
        }
        if (__nologin) {
            String cmd = event.getCommand();
            if ("PASS".equalsIgnoreCase(cmd) || "USER".equalsIgnoreCase(cmd)) {
                __writer.append(cmd);
                __writer.append(" *******"); // Don't bother with EOL marker
                // for this!
            } else {
                final String IMAP_LOGIN = "LOGIN";
                if (IMAP_LOGIN.equalsIgnoreCase(cmd)) { // IMAP
                    String msg = event.getMessage();
                    msg = msg.substring(0,
                            msg.indexOf(IMAP_LOGIN) + IMAP_LOGIN.length());
                    __writer.append(msg);
                    __writer.append(" *******"); // Don't bother with EOL
                    // marker for this!
                } else {
                    __writer.append(getPrintableString(event.getMessage()));
                }
            }
        } else {
            __writer.append(getPrintableString(event.getMessage()));
        }
        String w = __writer.toString();

        if (__logger.isDebugEnabled()) {
            __logger.debug("client request: " + w);
        }
    }

    private String getPrintableString(String msg) {
        if (__eolMarker == 0) {
            return msg;
        }
        int pos = msg.indexOf(SocketClient.NETASCII_EOL);
        if (pos > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(msg.substring(0, pos));
            sb.append(__eolMarker);
            sb.append(msg.substring(pos));
            return sb.toString();
        }
        return msg;
    }

    @Override
    public void protocolReplyReceived(ProtocolCommandEvent event) {
        StringBuilder __writer = new StringBuilder();
        if (__directionMarker) {
            __writer.append("< ");
        }
        __writer.append(event.getMessage());
        String w = __writer.toString();

        if (__logger.isDebugEnabled()) {
            __logger.debug("Server response:" + w);
        }
    }

}
