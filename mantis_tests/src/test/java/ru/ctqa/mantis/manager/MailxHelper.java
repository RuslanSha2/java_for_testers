package ru.ctqa.mantis.manager;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class MailxHelper extends HelperBase {
    Properties mailServerProperties;

    public MailxHelper(ApplicationManager manager) {
        super(manager);
        mailServerProperties = new Properties();
        mailServerProperties.put("mail.smtp.host", manager.property("james.smtpHost"));
        mailServerProperties.put("mail.smtp.port", manager.property("james.smtpPort"));
    }

    public void send(String sender, String addressee, String subject, String body) {
        Session session = Session.getDefaultInstance(mailServerProperties);
        MimeMessage messageToSend = new MimeMessage(session);

        try {
            messageToSend.setFrom(new InternetAddress(sender));
            messageToSend.addRecipient(Message.RecipientType.TO, new InternetAddress(addressee));
            messageToSend.setSubject(subject);
            messageToSend.setText(body);

            Transport.send(messageToSend);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}