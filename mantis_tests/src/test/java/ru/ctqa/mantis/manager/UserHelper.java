package ru.ctqa.mantis.manager;

import org.openqa.selenium.By;

public class UserHelper extends HelperBase {
    public UserHelper(ApplicationManager manager) {
        super(manager);
    }

    public void startCreation(String user, String email) {
        if (!manager.session().isLoggedIn())  {
            manager.session().login(manager.property("web.username"), manager.property("web.password"));
        }
        manager.driver().get(String.format("%s/manage_user_create_page.php", manager.property("web.baseUrl")));
        type(By.name("username"), user);
        type(By.name("realname"), user);
        type(By.name("email"), email);
        click(By.cssSelector("input[type='submit']"));
    }

    public void finishCreation(Object url, String password) {
        openUrl(url.toString());
        type(By.id("password"), password);
        type(By.id("password-confirm"), password);
        click(By.xpath("//button[@type='submit']"));
    }
}
