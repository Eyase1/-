package com.ttt.util.user;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class EmailUtils {

    private static final String EMAIL_ACCOUNT = "2762810581@qq.com"; // 发送方邮箱
    private static final String EMAIL_PASSWORD = "kwciedhaolidddgj"; // 发送方邮箱授权码
    private static final String EMAIL_HOST = "smtp.qq.com";
    private static final int SSL_PORT = 465; // QQ邮箱SSL端口

    public static void sendEmailCode(String email, String code) {
        Properties props = new Properties();
        props.put("mail.smtp.host", EMAIL_HOST);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", SSL_PORT);
        props.put("mail.smtp.socketFactory.port", SSL_PORT);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        // 使用 SSL 安全连接
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // 使用 SMTP 认证
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_ACCOUNT, EMAIL_PASSWORD);
            }
        });

        try {
            MimeMessage message = createEmail(session, email, code);
            Transport transport = session.getTransport();
            transport.connect(EMAIL_HOST, EMAIL_ACCOUNT, EMAIL_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static MimeMessage createEmail(Session session, String email, String code) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EMAIL_ACCOUNT));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
        message.setSubject("【营养管理系统】");

        // 修改模板路径为类路径相对路径
        String template = TemplateUtils.readHtmlTemplate("template/email_template.html");
        String htmlContent = template.replace("${code}", code);
        message.setContent(htmlContent, "text/html;charset=utf-8");

        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }

    public static String generateCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
