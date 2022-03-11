package com.viettel.etc.utils;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.Properties;

public class SendEmail {
//    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(SendEmail.class.getName());
//
//    /**
//     * Thong tin cau hinh mail
//     *
//     * @param ipHost
//     * @param port
//     * @param ipLocal
//     * @return
//     * @throws Exception
//     */
//    private static Properties getHostMail(String ipHost, String port, String ipLocal) throws Exception {
//        Properties props = new Properties();
//        props.put("mail.smtp.host", ipHost);
//        props.put("mail.smtp.localhost", ipLocal);
//        props.put("mail.smtp.socketFactory.port", port);
//        props.put("mail.smtp.socketFactory.class",
//                "javax.net.ssl.SSLSocketFactory");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.port", port);
//        return props;
//
//    }

//    /**
//     * Kiem tra user Pass lay session de gui mail
//     *
//     * @param props
//     * @param userMail
//     * @param passMail
//     * @return
//     * @throws Exception
//     */
//    private static Session getSessionMail(Properties props, final String userMail, final String passMail) throws Exception {
//        Session session = Session.getDefaultInstance(props,
//                new javax.mail.Authenticator() {
//                    @Override
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(userMail, passMail);
//                    }
//                });
//        return session;
//    }

//    /**
//     * Set noi dung mail gui di
//     *
//     * @param session
//     * @param mailFrom
//     * @param mailReceive
//     * @param subject
//     * @param content
//     * @return
//     * @throws Exception
//     */
//    private static Message getMessageForMail(Session session, String mailFrom, String mailReceive, String subject, String content) throws Exception {
//        try {
//            MimeMessage message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(mailFrom));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailReceive));
//            message.setSubject(subject);
//            message.setContent(content, "text/html;charset=utf-8");
//            message.saveChanges();
//            return message;
//        } catch (Exception e) {
//            LOGGER.error("getMessageForMail :", e);
//        }
//        return null;
////        return message;
//    }

//    /**
//     * Ham xu ly gui mail
//     *
//     * @param ipServerMail
//     * @param port
//     * @param ipLocal
//     * @param user
//     * @param pass
//     * @param mailFrom
//     * @param mailReceive
//     * @param subject
//     * @param content
//     * @param callId
//     * @return
//     */
//    private static boolean sendMailSSL(String ipServerMail, String port, String ipLocal, String user, String pass, String mailFrom,
//                                       String mailReceive, String subject, String content, long callId) {
//        boolean result = false;
//        try {
////            log.info(callId + " &&&&&&&&&&&&&&&              START gui email            &&&&&&&&&&&&&&& ");
////            log.info(callId + " ipServerMail = " + ipServerMail + ", port=" + port + ", ipLocal=" + ipLocal + ", user=" + user);
//            Properties props = getHostMail(ipServerMail, port, ipLocal);
//            Session session = getSessionMail(props, user, pass);
//            Message message = getMessageForMail(session, mailFrom, mailReceive, subject, content);
//            Transport.send(message);
//            result = true;
//        } catch (Exception e) {
////            log.error("EX SEND MAIL:" + e.getMessage(), e);
//            LOGGER.error("sendMailSSL :", e);
//        } finally {
////            log.info(callId + " &&&&&&&&&&&&&&&              END gui email            &&&&&&&&&&&&&&& KETQUA:" + result);
//        }
//        return result;
//    }


//    public static void sendMail(String customerName, String mailReceive, String user, String pass) {
//        String subbject = "[ePass] Thông báo thông tin tài khoản";
//
//        String content = "<!DOCTYPE HTML>\n"
//                + "<meta charset=\"UTF-8\">\n"
//                + "<html>\n"
//                + "<body>\n"
//                + "<p><b>THÔNG BÁO THÔNG TIN TÀI KHOẢN</b></p>\n"
//                + "<br>\n"
//                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b><i> Kính gửi quý khách hàng: " + customerName + "</b></i></p>\n"
//                + "<p>ePass xin gửi tới quý khách hàng thông tin tài khoản : </p>\n"
//                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Tên tài khoản:<b> " + user + "</b></p>\n"
//                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Mật khẩu:<b> " + pass + "</b></p>		\n"
//                + "<br>\n"
//                + "<p>Để được hỗ trợ trực tiếp quý khách vui lòng liên hệ hotline CSKH: <b>19009019</b></p>\n"
//                + "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b><i>Trân trọng cảm ơn quý khách đã tin tưởng sử dụng ePass và đồng hành cùng chúng tôi!</b></i></p>\n"
//                + "</body>\n"
//                + "</html>";
//        sendMailSSL("10.60.158.27", "25", "125.235.240.36", "vetc@viettel.com.vn", "Hanoi@123",
//                "vetc@viettel.com.vn", mailReceive, subbject, content, 0);
//    }
}
