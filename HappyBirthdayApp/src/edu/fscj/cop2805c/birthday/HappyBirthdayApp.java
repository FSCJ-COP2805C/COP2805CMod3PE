// HappyBirthdayApp.java
// D. Singletary
// 1/29/23
// wish multiple users a happy birthday

package edu.fscj.cop2805c.birthday;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TimeZone;

// utility class with constant list of desired timezones (US/)
class DesiredTimeZones {
    public static final ArrayList<String> ZONE_LIST = new ArrayList<>();

    public DesiredTimeZones() {
        String[] availableTimezones = TimeZone.getAvailableIDs();
        for (String s : availableTimezones) {
            if (s.length() >= 3 && s.substring(0, 3).equals("US/")) {
                ZONE_LIST.add(s);
            }
        }
    }

    // show the list of zones as a numeric menu
    public void showMenu() {
        int menuCount = 1;
        for (String s : ZONE_LIST)
            System.out.println(menuCount++ + ". " + s);
    }
}

// main mpplication class
public class HappyBirthdayApp {
    //private User user;
    private ArrayList<User> birthdays = new ArrayList<>();

    public HappyBirthdayApp() { }

    // get user's information
    public User getUserInfo(DesiredTimeZones dzt) {
        String fName = "", lName = "";
        int year = 0, month = 0, day = 0;
        int zoneChoice = 0;
        User user = null;
        LocalDateTime date;
        ZoneId zone;
        ZonedDateTime zdt;

        // first and last name
        Scanner kbd = new Scanner(System.in);
        prompt("Please enter the user's first name");
        fName = kbd.nextLine();
        prompt("Please enter the user's last name");
        lName = kbd.nextLine();

        // birthday
        prompt("Please enter the year of the user's birthday");
        year = kbd.nextInt();
        prompt("Please enter the month of the user's birthday");
        month = kbd.nextInt();
        prompt("Please enter the day of the user's birthday");
        day = kbd.nextInt();
        date = LocalDateTime.of(year, month, day, 0, 0, 0);

        // timezone
        dzt.showMenu();
        prompt("Please select the number of the user's timezone from the menu");
        zoneChoice = kbd.nextInt();
        // get selected timezone from list (adjust choice for 0-based index)
        zone = ZoneId.of(dzt.ZONE_LIST.get(zoneChoice - 1));
        zdt = ZonedDateTime.of(LocalDateTime.from(date), zone);
        user = new User(fName, lName, zdt);
        return user;

    }

    // show prompt msg with no newline
    public static void prompt(String msg) {
        System.out.print(msg + ": ");
    }

    // compare current month and day to user's data
    // to see if it is their birthday
    public boolean isBirthday(User u) {
        boolean result = false;

        LocalDate today = LocalDate.now();
        if (today.getMonth() == u.getBirthday().getMonth() &&
                today.getDayOfMonth() == u.getBirthday().getDayOfMonth())
            result = true;

        return result;
    }

    // add multiple birthdays
    public void addBirthdays(User... users) {
        for (User u : users) {
            birthdays.add(u);
        }
    }

    // main program
    public static void main(String[] args) {

        HappyBirthdayApp hba = new HappyBirthdayApp();

        // create a list of desired timezones to use for our app, we'll use the US/ zones
        DesiredTimeZones dzt = new DesiredTimeZones();

        // loop while the user wants to add more birthdays
        boolean more = true;
        Scanner kbd = new Scanner(System.in);
        while (more == true) {
            prompt("Would you like to enter a user's birthday information? (y/n)");
            String yn = kbd.nextLine();
            if (yn.toLowerCase().equals("y")) {
                hba.birthdays.add(hba.getUserInfo(dzt));
            } else
                more = false;
        }

        // test the varargs method by creating multiple birthdays and adding them
        // names were generated using the random name generator at http://random-name-generator.info/
        // be sure to test the positive case (today is someone's birthday as well as negative
        User u1 = new User("Sally", "Roberts",
                ZonedDateTime.of(2001, 6, 2,
                        0, 0, 0, 0,
                        ZoneId.of("US/Mountain")));
        User u2 = new User("Dianne", "Romero",
                ZonedDateTime.of(1997, 9, 16,
                        0, 0, 0, 0,
                        ZoneId.of("US/Central")));
        User u3 = new User("Edwin", "Peterson",
                ZonedDateTime.of(1991, 1, 29,
                        0, 0, 0, 0,
                        ZoneId.of("US/Eastern")));
        hba.addBirthdays(u1, u2, u3);

        // show the birthdays
        if (!hba.birthdays.isEmpty()) {
            System.out.println("Hello, here are the birthdays:");
            for (User u : hba.birthdays) {
                System.out.println(u);
                // see if today is their birthday
                if (!hba.isBirthday(u))
                    System.out.println("Sorry, today is not their birthday.");
                else
                    System.out.println("Happy Birthday to " + u.getName());
            }
        }
    }
}

// user class
class User {
    private StringBuilder name;
    private ZonedDateTime birthday;

    public User(String fName, String lName, ZonedDateTime birthday) {
        this.name = new StringBuilder();
        this.name.append(fName).append(" ").append(lName);
        this.birthday = birthday;
    }

    public StringBuilder getName() {
        return name;
    }

    public ZonedDateTime getBirthday() {
        return birthday;
    }

    //public void setBirthday(ZonedDateTime birthday) {
    //    this.birthday = birthday;
    //}

    @Override
    public String toString() {
        String s = this.name + "," + this.birthday;
        return s;
    }
}
