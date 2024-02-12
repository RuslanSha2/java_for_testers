package ru.ctqa.mantis.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.ctqa.mantis.common.CommonFunctions;
import ru.ctqa.mantis.model.DeveloperMailUser;

import java.time.Duration;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class UserRegistrationsTests extends TestBase {

    public static Stream<String> randomUser() {
        return Stream.of(CommonFunctions.randomString(8));
    }

    @ParameterizedTest
    @MethodSource("randomUser")
    void canRegisterUser(String username) throws InterruptedException {
        // создать пользователя (адрес) на почтовом сервисе (JamesHelper)
        var email = String.format("%s@localhost", username);
        var password = "password";
        app.jamesCli().addUser(email, password);

        // заполняем форму создания и отправляем (SessionHelper)
        app.session().login(
                app.property("web.username"),
                app.property("web.password"));
        app.session().createUser(username, email);
        Assertions.assertTrue(app.session().existUser(username, email));

        // ждём почту (MailHelper)
        var duration = Duration.ofSeconds(60);
        var messages = app.mail().receive(email, password, duration);
        Assertions.assertEquals(1, messages.size());

        // извлекаем ссылку из письма (MailHelper)
        var text = messages.get(0).content();
        var pattern = Pattern.compile("http://\\S*");
        var matcher = pattern.matcher(text);
        Assertions.assertTrue(matcher.find());
        var url = text.substring(matcher.start(), matcher.end());

        // проходим по ссылке и завершаем регистрацию пользователя (SessionHelper)
        app.session().confirmPassword(url, password);
        Assertions.assertTrue(app.session().isLoginForm());

        // проверяем, что пользователь может залогиниться (HttpSessionHelper)
        app.http().login(username, password);
        Assertions.assertTrue(app.http().isLoggedIn());
    }

    @ParameterizedTest
    @MethodSource("randomUser")
    void canCreateUser(String user) throws InterruptedException {
        var email = String.format("%s@localhost", user);
        var password = "password";
        app.jamesApi().addUser(email, password);

        app.user().startCreation(user, email);

        var messages = app.mail().receive(email, password, Duration.ofSeconds(10));
        var url = CommonFunctions.extractUrl(messages.get(0).content());

        app.user().finishCreation(url, password);

        app.http().login(user, password);
        Assertions.assertTrue(app.http().isLoggedIn());
    }

    DeveloperMailUser user;

    @Test
    void canCreateUserInDevMail() throws InterruptedException {
//        var password = "password";
//        user = app.developerMail().addUser();
//        var email = String.format("%s@developer.mail.com", user.name());
//
//        app.user().startCreation(user.name(), email);
//
//        var message = app.developerMail().receive(user, Duration.ofSeconds(10));
//        var url = CommonFunctions.extractUrl(message);
//
//        app.user().finishCreation(url, password);
//
//        app.http().login(user.name(), password);
//        Assertions.assertTrue(app.http().isLoggedIn());
    }

    @AfterEach
    void deleteMailUser() {
//        app.developerMail().deleteUser(user);
    }
}