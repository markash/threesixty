package za.co.yellowfire.threesixty.domain;

import java.util.Arrays;
import java.util.Collection;

public abstract class DummyDataGenerator {

    static Collection<DashboardNotification> randomNotifications() {
        DashboardNotification n1 = new DashboardNotification();
        n1.setId(1);
        n1.setFirstName("Mark");
        n1.setLastName("Ashworth");
        n1.setAction("created a new report");
        n1.setPrettyTime("25 minutes ago");
        n1.setContent("ABC");

        DashboardNotification n2 = new DashboardNotification();
        n2.setId(2);
        n2.setFirstName("Mark");
        n2.setLastName("Ashworth");
        n2.setAction("changed the schedule");
        n2.setPrettyTime("2 days ago");
        n2.setContent("DEF");

        return Arrays.asList(n1, n2);
    }
}