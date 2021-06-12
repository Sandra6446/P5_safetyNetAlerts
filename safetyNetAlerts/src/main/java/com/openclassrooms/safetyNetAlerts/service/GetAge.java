package com.openclassrooms.safetyNetAlerts.service;

import org.joda.time.DateTime;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;


public class GetAge {

    public static int getAge(String birthdate) {
        DateTime today = new DateTime();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy").withLocale(Locale.FRENCH);
        DateTime date = formatter.parseDateTime(birthdate);
        Years years = Years.yearsBetween(date, today);
        return years.getYears();
    }
}
