package ru.ctqa.mantis.tests;

import org.junit.jupiter.api.*;
import ru.ctqa.mantis.common.CommonFunctions;

import java.time.Duration;
import java.util.regex.Pattern;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MailTests extends TestBase {
    @Test
    @Order(1)
    void canSendEmail() {
        var body = String.format("Hello!\nhttp://%s", CommonFunctions.randomString(10));
        app.mailx().send(
                "administrator@localhost",
                "user1@localhost",
                "Urgent message",
                body);
    }

    @Test
    @Order(2)
    void canReceiveEmail() {
        var messages = app.mail().receive("user1@localhost", "password", Duration.ofSeconds(60));
        Assertions.assertEquals(1, messages.size());
        System.out.println(messages);
    }

    @Test
    @Order(3)
    void canExtractUrl() {
        var messages = app.mail().receive("user1@localhost", "password", Duration.ofSeconds(60));
        var text = messages.get(0).content();
        var pattern = Pattern.compile("http://\\S*");
        var matcher = pattern.matcher(text);
        if (matcher.find()) {
            var url = text.substring(matcher.start(), matcher.end());
            System.out.println(url);
        }
    }

    @Test
    @Order(4)
    void canDrainInbox() {
        app.mail().drain("user1@localhost", "password");
    }
 }
