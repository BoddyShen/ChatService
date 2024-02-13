package com.group.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateUtilTest {
    String date;
    String pattern;
    LocalDate birthday;

    @BeforeEach
    void setUp() {
        date = "2019-01-01";
        pattern = "yyyy-MM-dd";
        birthday = LocalDate.of(1989, 3, 9);
    }

    @Test
    public void testDateFormatterWithValidDateStringPattern() {
        LocalDate result = DateUtil.dateFormatter(date, pattern);
        assertEquals(result, LocalDate.of(2019, 1, 1));
    }

    @Test
    public void testDateFormatterWithValidDateString() {
        LocalDate result = DateUtil.dateFormatter(date);
        assertEquals(result, LocalDate.of(2019, 1, 1));
    }
    @Test
    public void testGetAgeWithValidBirthday() {
        int expectedAge = 34;
        int actualAge = DateUtil.getAge(birthday);
        assertEquals(expectedAge, actualAge);
    }
}
