package ru.ctqa.mantis.manager.developermail;

import java.util.List;

public record GetIdsResponse(boolean success, Object errors, List<String> result) {
}
