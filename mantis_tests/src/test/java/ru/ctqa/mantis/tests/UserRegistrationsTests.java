package ru.ctqa.mantis.tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class UserRegistrationsTests extends TestBase {

    @ParameterizedTest
    @ValueSource(strings = {"user1"})
    void canRegisterUser(String username) {
        var email = String.format("%s@localhost", username);
        // создать пользователя (адрес) на почтовом сервисе (JamesHelper)
        // заполняем форму создания и отправляем (браузер)
        // ждём почту (MailHelper)
        // извлекаем ссылку из письма
        // проходим по ссылке и завершаем регистрацию пользователя (браузер)
        // проверяем, что пользователь может залогиниться (HttpSessionHelper)
        System.out.println(email);
    }
}
