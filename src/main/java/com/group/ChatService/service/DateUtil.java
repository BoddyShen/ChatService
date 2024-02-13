package com.group.ChatService.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * Parses a string representation of a date into a LocalDate object using the specified date pattern.
     *
     * @param date The string date representation
     * @param datePattern The optional date pattern to use for parsing. If not provided, use the default pattern.
     * @return The parsed LocalDate object.
     */
    public static LocalDate dateFormatter(String date, String... datePattern) {
        String pattern = (datePattern.length > 0) ? datePattern[0] : DATE_PATTERN;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate formattedDate = LocalDate.parse(date, formatter);
        return formattedDate;
    }

    public static Integer getAge(LocalDate birthday) {
        Integer age = Period.between(birthday, LocalDate.now()).getYears();
        return age;
    }
}
