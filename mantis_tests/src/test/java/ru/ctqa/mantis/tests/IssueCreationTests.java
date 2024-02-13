package ru.ctqa.mantis.tests;

import ru.ctqa.mantis.common.CommonFunctions;
import ru.ctqa.mantis.model.IssueData;
import org.junit.jupiter.api.Test;

public class IssueCreationTests extends TestBase {
    @Test
    void canCreateIssueRest() {
        app.rest().createIssue(new IssueData()
                .withSummary(CommonFunctions.randomString(10))
                .withDescription(CommonFunctions.randomString(10))
                .withProject(1L));
    }

    @Test
    void canCreateIssueSoap() {
        app.soap().createIssue(new IssueData()
                .withSummary(CommonFunctions.randomString(10))
                .withDescription(CommonFunctions.randomString(10))
                .withProject(1L));
    }
}
