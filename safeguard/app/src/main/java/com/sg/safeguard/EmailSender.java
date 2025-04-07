package com.sg.safeguard;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
  private static final String EMAIL = "safeguard.dtu@gmail.com"; // Đổi email
  private static final String PASSWORD = "wxqmrjrrtxthcvmt"; // Đổi mật khẩu

  // Phương thức gửi email xác thực
  public static void sendVerificationEmail(String toEmail, String code) {
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.timeout", "10000");

    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(EMAIL, PASSWORD);
      }
    });

    new Thread(() -> {
      try {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Verification Code");
        message.setText("Your verification code is: " + code);
        Transport.send(message);
      } catch (MessagingException e) {
        e.printStackTrace();
      }
    }).start();
  }
}
