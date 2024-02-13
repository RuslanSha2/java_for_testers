package ru.ctqa.mantis.manager;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import okhttp3.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.util.Date;
import java.util.Properties;

public class JamesApiHelper extends HelperBase {
    public static final MediaType JSON = MediaType.get("application/json");
    OkHttpClient client;

    public JamesApiHelper(ApplicationManager manager) {
        super(manager);
        client = new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(new CookieManager())).build();
    }

    public void addUser(String email, String password) {
        RequestBody body = RequestBody.create(String.format("{\"password\":\"%s\"}", password), JSON);
        Request request = new Request.Builder()
                .url(String.format("%s/users/%s", manager.property("james.apiBaseUrl"), email))
                .put(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmail(String sender, String addressee, String subject, String message) {
        var mimeMessage = new MimeMessage(Session.getInstance(new Properties()));

        try {
            mimeMessage.addHeader("Content-type", "text/HTML; charset=UTF-8");
            mimeMessage.addHeader("format", "flowed");
            mimeMessage.addHeader("Content-Transfer-Encoding", "8bit");
            try {
                mimeMessage.setFrom(new InternetAddress(sender, "NoReply-JD"));
                mimeMessage.setReplyTo(InternetAddress.parse(sender, false));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            mimeMessage.setSubject(subject, "UTF-8");
            mimeMessage.setText(message, "UTF-8");
            mimeMessage.setSentDate(new Date());
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(addressee, false));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(String.format("{%s}", mimeMessage.toString()), JSON);
        Request request = new Request.Builder()
                .url(String.format("%s/mail-transfer-service", manager.property("james.apiBaseUrl")))
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}