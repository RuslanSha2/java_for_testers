package ru.ctqa.mantis.manager.developermail;

import ru.ctqa.mantis.model.DeveloperMailUser;

public record AddUserResponse(boolean success, Object errors, DeveloperMailUser result) {
}
