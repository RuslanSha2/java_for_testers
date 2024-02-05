package ru.ctqa.mantis.manager;

import org.openqa.selenium.By;

public class SessionHelper extends HelperBase {
    public SessionHelper(ApplicationManager manager) {
        super(manager);
    }

    public void login(String user, String password) {
        type(By.name("username"), user);
        click(By.cssSelector("input[type='submit']"));
        type(By.name("password"), password);
        click(By.cssSelector("input[type='submit']"));
    }

    public void createUser(String username, String email) throws InterruptedException {
        openUsersPage();
        initUserCreation();
        fillUserForm(username, email);
        submitUserCreation();
        returnToUsersPage();
    }

    public boolean existUser(String username, String email) {
        return isElementPresent(By.xpath(String.format("//td[contains(.,'%s')]", username)))
                && isElementPresent(By.xpath(String.format("//td[contains(.,'%s')]", email)));
    }

    public void confirmPassword(String url, String password) {
        openUrl(url);
        type(By.id("password"), password);
        type(By.id("password-confirm"), password);
        click(By.xpath("//button[@type='submit']"));
    }

    public boolean isLoggedIn() {
        return isElementPresent(By.cssSelector("span.user-info"));
    }

    public boolean isLoginForm() {
        return isElementPresent(By.cssSelector("input[value='Login']"));
    }

    private void openUsersPage() {
        click(By.xpath("//span[contains(.,'Manage')]"));
        click(By.linkText("Users"));
    }

    private void initUserCreation() {
        click(By.linkText("Create New Account"));
    }

    private void fillUserForm(String username, String email) {
        type(By.id("user-username"), username);
        type(By.id("user-realname"), username);
        type(By.id("email-field"), email);
    }

    private void submitUserCreation() {
        if (isElementPresent(By.linkText("Proceed"))) {
            click(By.linkText("Proceed"));
        }
        click(By.xpath("//input[@value='Create User']"));
    }

   private void returnToUsersPage() throws InterruptedException {
       while (!isElementPresent(By.linkText("Users"))) {
           Thread.sleep(500);
       }
       click(By.linkText("Users"));
   }
}
