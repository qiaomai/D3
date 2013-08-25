package us.shareby.core.notification;

import com.google.common.base.Strings;
import com.google.common.io.Closeables;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.net.io.Util;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.commons.net.smtp.SimpleSMTPHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import us.shareby.core.notification.ClientPrintCommandListener;
import us.shareby.core.notification.smtpclient.AuthenticatingSMTPClient;
import us.shareby.core.service.ShareVelocity;

import javax.annotation.Resource;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author chengdong
 */
@Service
public class TransportService {

    private static Logger logger = LoggerFactory.getLogger(TransportService.class);

    /**
     * UTF-8
     */
    private static final String DEFAULT_CHARSET = "UTF-8";
    /**
     * test/html
     */
    private static final String DEFAULT_CONTENT_TYPE = "text/html";

    /**
     * TODO; default smtp server
     */
    @Value("${mail.notification.smtp.server}")
    private String defaultSmtpServer;

    /**
     * default port
     */
    @Value("${mail.notification.smtp.port}")
    private int defaultPort;
    /**
     * default sender
     */
    @Value("${mail.notification.sender}")
    private String defaultSender;

    @Value("${mail.notification.pwd}")
    private String mailPassword;

    /**
     * velocity
     */
    @Resource(name = "velocity")
    private ShareVelocity velocity;

    /**
     * @param server
     * @param port
     * @param sender
     * @param password
     * @param to
     * @param subject
     * @param
     */
    public void sendMailNotification(String server, int port, String sender,
                                     String password, String to, String subject, String htmlTemplate,
                                     Map<String, Object> htmlValuesMap, String textTemplate,
                                     Map<String, Object> textValuesMap) {


        InputStream htmlInput = null;
        InputStream textInput = null;

        try {
            htmlInput = velocity.getInputStreamFromTemplate(htmlValuesMap,
                    htmlTemplate);



            if (!Strings.isNullOrEmpty(textTemplate)) {
                textInput = velocity.getInputStreamFromTemplate(textValuesMap,
                        textTemplate);

            }


            if (!StringUtils.isBlank(password)) {

                if (htmlInput != null && textInput != null) {
                    sendAlternativeMailWithAuth(server, port, sender, password,
                            to, subject, htmlInput, textInput,
                            this.DEFAULT_CHARSET);
                } else {
                    this.sendHtmlMailWithAuth(server, port, sender, password,
                            to, subject, htmlInput, this.DEFAULT_CHARSET,
                            this.DEFAULT_CONTENT_TYPE);
                }

            } else {
                if (htmlInput != null && textInput != null) {
                    sendAlternativeMail(server, port, sender, to, subject,
                            htmlInput, textInput, this.DEFAULT_CHARSET);
                } else {
                    this.sendHtmlMail(server, port, sender, to, subject,
                            htmlInput, this.DEFAULT_CHARSET,
                            this.DEFAULT_CONTENT_TYPE);
                }

            }
        } finally {
            Closeables.closeQuietly(htmlInput);
            Closeables.closeQuietly(textInput);
            htmlInput = null;
            textInput = null;
        }


    }

    /**
     * send mail
     *
     * @param server
     * @param port
     * @param sender
     * @param password
     * @param to
     * @param subject
     * @param
     * @param charset
     * @param
     */
    public void sendAlternativeMailWithAuth(String server, int port,
                                            String sender, String password, String to, String subject,
                                            InputStream htmlInputStream, InputStream textInputStream,
                                            String charset) {

        if (Strings.isNullOrEmpty(server)) {
            server = defaultSmtpServer;
        }

        if (port <= 0) {
            port = defaultPort;
        }

        if (Strings.isNullOrEmpty(sender)) {
            sender = this.defaultSender;
        }

        if (!validatePwdParameter(password)) {
            return;
        }

        if (htmlInputStream == null || textInputStream == null) {
            logger.error("send mail error, for send content is null");
            return;
        }

        if (Strings.isNullOrEmpty(subject)) {
            subject = "";
        }

        if (Strings.isNullOrEmpty(charset)) {
            charset = DEFAULT_CHARSET;
        }

        try {

            logger.debug("sender=" + sender + "|password=" + password + "|to=" + to + "|subject=" + subject + "|");

            HtmlEmail email = new HtmlEmail();
            email.setSmtpPort(port);
            email.setHostName(server);
            email.setCharset(charset);
            email.setSubject(MimeUtility.encodeText(subject));
            email.setFrom(sender);
            email.addTo(to);
            email.setAuthentication(sender, password);

            MimeMultipart content = new MimeMultipart("alternative");

            MimeBodyPart text = new MimeBodyPart();
            StringWriter writer = new StringWriter();
            BufferedReader bufferReader = new BufferedReader(
                    new InputStreamReader(textInputStream));
            Util.copyReader(bufferReader, writer);
            text.setText(writer.toString(), charset);

            MimeBodyPart html = new MimeBodyPart();

            StringWriter writer2 = new StringWriter();
            BufferedReader bufferReader2 = new BufferedReader(
                    new InputStreamReader(htmlInputStream));
            Util.copyReader(bufferReader2, writer2);

            html.setContent(writer2.toString(), "text/html;charset=" + charset);

            content.addBodyPart(text);
            content.addBodyPart(html);

            email.setContent(content, content.getContentType());
            email.setSentDate(new Date());


            email.send();

        } catch (Exception e) {
            logger.error("send alternative mail exception", e);
            logger.debug("sender=" + sender + "|password=" + "" + "|to=" + to + "|subject=" + subject + "|" + "result:" + e.getMessage());
        }
    }

    /**
     * send mail
     *
     * @param server
     * @param port
     * @param sender
     * @param
     * @param to
     * @param subject
     * @param
     * @param charset
     * @param
     */
    public void sendAlternativeMail(String server, int port, String sender,
                                    String to, String subject, InputStream htmlInputStream,
                                    InputStream textInputStream, String charset) {

        if (Strings.isNullOrEmpty(server)) {
            server = defaultSmtpServer;
        }

        if (port <= 0) {
            port = defaultPort;
        }

        if (Strings.isNullOrEmpty(sender)) {
            sender = this.defaultSender;
        }

        if (htmlInputStream == null || textInputStream == null) {
            logger.error("send mail error, for send content is null");
            return;
        }

        if (Strings.isNullOrEmpty(subject)) {
            subject = "";
        }

        if (Strings.isNullOrEmpty(charset)) {
            charset = DEFAULT_CHARSET;
        }

        try {
            logger.debug("sender=" + sender + "|password=" + "" + "|to=" + to + "|subject=" + subject + "|");
            HtmlEmail email = new HtmlEmail();
            email.setSmtpPort(port);
            email.setHostName(server);
            email.setCharset(charset);
            email.setSubject(MimeUtility.encodeText(subject));
            email.setFrom(sender);
            email.addTo(to);
            MimeMultipart content = new MimeMultipart("alternative");
            MimeBodyPart text = new MimeBodyPart();
            StringWriter writer = new StringWriter();
            BufferedReader bufferReader = new BufferedReader(
                    new InputStreamReader(textInputStream));
            Util.copyReader(bufferReader, writer);
            text.setText(writer.toString(), charset);

            MimeBodyPart html = new MimeBodyPart();

            StringWriter writer2 = new StringWriter();
            BufferedReader bufferReader2 = new BufferedReader(
                    new InputStreamReader(htmlInputStream));
            Util.copyReader(bufferReader2, writer2);

            html.setContent(writer2.toString(), "text/html;charset=" + charset);

            content.addBodyPart(text);
            content.addBodyPart(html);

            email.setContent(content, content.getContentType());
            email.setSentDate(new Date());

            email.send();

        } catch (Exception e) {
            logger.error("send alternative mail exception", e);
            logger.debug("sender=" + sender + "|password=" + "" + "|to=" + to + "|subject=" + subject + "|" + "result:" + e.getMessage());
        }

        logger.debug("sender=" + sender + "|password=" + "" + "|to=" + to + "|subject=" + subject + "|" + "result:success");
    }

    /**
     * send mail
     *
     * @param server
     * @param port
     * @param sender
     * @param password
     * @param to
     * @param subject
     * @param inputStream
     * @param charset
     * @param contentType
     */
    public void sendHtmlMailWithAuth(String server, int port, String sender,
                                     String password, String to, String subject,
                                     InputStream inputStream, String charset, String contentType) {

        List<String> recipientList = new ArrayList<String>();
        recipientList.add(to);

        sendHtmlMailWithAuth(server, port, sender, password, recipientList,
                null, null, subject, inputStream, charset, contentType);

    }

    /**
     * @param server
     * @param sender
     * @param password
     * @param recipientList
     * @param ccRecipientList
     * @param bccRecipientList
     * @param subject
     * @param inputStream
     */
    public void sendHtmlMailWithAuth(String server, int port, String sender,
                                     String password, List<String> recipientList,
                                     List<String> ccRecipientList, List<String> bccRecipientList,
                                     String subject, InputStream inputStream, String charset,
                                     String contentType) {

        if (Strings.isNullOrEmpty(server)) {
            server = defaultSmtpServer;
        }

        if (port <= 0) {
            port = defaultPort;
        }

        if (Strings.isNullOrEmpty(sender)) {
            sender = this.defaultSender;
        }

        if (!validateParamWithoutPwd(server, sender, recipientList)
                || !validatePwdParameter(password)) {
            return;
        }

        if (inputStream == null) {
            logger.error("send mail error, for send content is null");
            return;
        }

        if (Strings.isNullOrEmpty(subject)) {
            subject = "";
        }

        AuthenticatingSMTPClient client = null;
        try {

            client = new AuthenticatingSMTPClient("TLS", "UTF-8");

            client.addProtocolCommandListener(new ClientPrintCommandListener(
                    logger, false));

            client.connect(server);

            if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
                client.disconnect();
                logger.error("SMTP server refused connection.");
                return;
            }
            // send ehlo
            boolean loginResult = client.elogin();

            if (!loginResult) {
                logger.error("login by ehlo fail for  server=" + server + ":port="
                        + port);
                return;
            }
            // send auth plain
            boolean result = client.auth(
                    AuthenticatingSMTPClient.AUTH_METHOD.PLAIN, sender,
                    password);
            if (!result) {
                logger.error("auth fail: for sender=" + sender + ":password="
                        + password);
                return;
            }

            for (String to : recipientList) {
                logger.debug("sender=" + sender + "|password=" + password + "|to=" + to + "|subject=" + subject + "|");
            }


            handleSendingMail(client, sender, recipientList, ccRecipientList,
                    bccRecipientList, subject, inputStream, charset,
                    contentType);

        } catch (Exception e) {
            logger.error("send mail with auth exception", e);
        } finally {
            if (client != null) {
                try {

                    client.logout();
                    client.disconnect();
                } catch (IOException e) {
                    logger.error("close client exception", e);
                }
            }

            Closeables.closeQuietly(inputStream);

        }

    }

    /**
     * send mail
     *
     * @param server
     * @param port
     * @param sender
     * @param
     * @param to
     * @param subject
     * @param inputStream
     * @param charset
     * @param contentType
     */
    public void sendHtmlMail(String server, int port, String sender, String to,
                             String subject, InputStream inputStream, String charset,
                             String contentType) {

        List<String> recipientList = new ArrayList<String>();
        recipientList.add(to);

        sendHtmlMail(server, port, sender, recipientList, null, null, subject,
                inputStream, charset, contentType);

    }

    /**
     * send mail
     *
     * @param server
     * @param port
     * @param sender
     * @param recipientList
     * @param ccRecipientList
     * @param bccRecipientList
     * @param subject
     * @param inputStream
     * @param charset
     * @param contentType
     */
    public void sendHtmlMail(String server, int port, String sender,
                             List<String> recipientList, List<String> ccRecipientList,
                             List<String> bccRecipientList, String subject,
                             InputStream inputStream, String charset, String contentType) {

        if (Strings.isNullOrEmpty(server)) {
            server = defaultSmtpServer;
        }

        if (port <= 0) {
            port = defaultPort;
        }

        if (Strings.isNullOrEmpty(sender)) {
            sender = this.defaultSender;
        }

        if (!validateParamWithoutPwd(server, sender, recipientList)) {
            return;
        }
        if (Strings.isNullOrEmpty(subject)) {
            subject = "";
        }

        if (inputStream == null) {
            logger.error("send mail error, for send content is null");
            return;
        }

        SMTPClient client = null;

        try {
            client = new SMTPClient("UTF-8");
            client.addProtocolCommandListener(new ClientPrintCommandListener(
                    logger, false));
            client.connect(server);

            if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
                client.disconnect();
                logger.error("SMTP server refused connection.");
                return;
            }
            boolean loginResult = client.login();

            if (!loginResult) {
                logger.error("login by helo fail for  server=" + server + ":port="
                        + port);
                return;
            }
            boolean sendMailFrom = client.setSender(sender);
            if (!sendMailFrom) {
                logger.error("send mail from cmd error  server=" + server
                        + ":port=" + port);
                return;
            }
            handleSendingMail(client, sender, recipientList, ccRecipientList,
                    bccRecipientList, subject, inputStream, charset,
                    contentType);

        } catch (Exception e) {
            logger.error("send mail without exception", e);
        } finally {
            if (client != null) {
                try {

                    client.logout();
                    client.disconnect();
                } catch (IOException e) {
                    logger.error("close client exception", e);
                }
            }

            Closeables.closeQuietly(inputStream);
        }

    }

    private void handleSendingMail(SMTPClient client, String sender,
                                   List<String> recipientList, List<String> ccRecipientList,
                                   List<String> bccRecipientList, String subject,
                                   InputStream inputStream, String charset, String contentType)
            throws IOException {

        // handle to:
        int size = recipientList.size();

        boolean rcptResult = false;

        StringBuffer recipientStr = new StringBuffer();
        if (recipientList != null) {

            for (int i = 0; i < size; i++) {
                String recpt = recipientList.get(i);

                boolean result = client.addRecipient(recpt);
                if (result) {
                    recipientStr.append(recpt);
                    recipientStr.append(",");
                    rcptResult = true;
                }
            }
        }
        if (!rcptResult) {
            logger.error("rcpt to cmd error");
            return;
        }

        String rcptStr = recipientStr.toString();
        rcptStr = rcptStr.substring(0, rcptStr.length() - 1);
        SimpleSMTPHeader header = new SimpleSMTPHeader(sender, rcptStr,
                MimeUtility.encodeText(subject));

        if (Strings.isNullOrEmpty(contentType)) {
            contentType = DEFAULT_CONTENT_TYPE;
        }

        // set contentType and charset
        if (Strings.isNullOrEmpty(charset)) {
            charset = DEFAULT_CHARSET;
        }

        contentType = contentType + ";charset=" + charset;
        header.addHeaderField("Content-type", contentType);
        // header.addHeaderField("Content-Transfer-Encoding",
        // "quoted-printable");

        // handle cc:
        if (ccRecipientList != null) {

            for (String cc : ccRecipientList) {
                boolean result = client.addRecipient(cc);
                if (result) {
                    header.addCC(cc);
                }

            }
        }
        // handle bcc:
        if (bccRecipientList != null) {
            for (String bcc : bccRecipientList) {
                client.addRecipient(bcc);
            }
        }
        // send data
        Writer writer = client.sendMessageData();
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(
                inputStream));

        if (writer != null) {
            writer.write(header.toString());
            Util.copyReader(bufferReader, writer);
            writer.close();

            client.completePendingCommand();
        }

    }

    /**
     * validate sender parameter
     *
     * @param
     * @param
     * @param password
     * @param
     */
    private boolean validatePwdParameter(String password) {

        if (Strings.isNullOrEmpty(password)) {
            logger.error("!!!!! Password should not be null,please  check it");
            return false;
        }

        return true;

    }

    /**
     * validate sender parameter
     *
     * @param server
     * @param sender
     * @param
     * @param recipientList
     */
    private boolean validateParamWithoutPwd(String server, String sender,
                                            List<String> recipientList) {
        if (Strings.isNullOrEmpty(server)) {
            logger.error("!!!!!Server should not be null,please check it");
            return false;
        }

        if (Strings.isNullOrEmpty(sender)) {
            logger.error("!!!!!Sender or Password should not be null,please  check it");
            return false;
        }

        if (recipientList == null || recipientList.size() == 0) {
            logger.error("!!!!!Recipient  list should not be null or zero");
            return false;
        }

        return true;
    }

}
