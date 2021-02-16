package ru.job4j.html;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class SqlRuParseTest {

    @Test
    public void parseDate1() {
        LocalDateTime date = LocalDateTime.of(2021,2,8,23,30);
        assertEquals(date, SqlRuParse.parseDate("8 фев 21, 23:30"));
    }

    @Test
    public void parseDate2() {
        LocalDateTime date = LocalDateTime.of(2020, 3,12,1,43);
        assertEquals(date, SqlRuParse.parseDate("12 мар 20, 01:43"));
    }

    @Test
    public void parseDate3() {
        LocalDateTime date = LocalDateTime.of(2019, 12,2,22, 29);
        assertEquals(date, SqlRuParse.parseDate("2 дек 19, 22:29"));
    }

    @Test
    public void parseDateToday() {
        LocalDateTime date = LocalDate.now().atTime(22, 29);
        assertEquals(date, SqlRuParse.parseDate("сегодня, 22:29"));
    }

    @Test
    public void parseDateYesterday() {
        LocalDateTime date = LocalDate.now().atTime(22, 29).minusDays(1);
        assertEquals(date, SqlRuParse.parseDate("вчера, 22:29"));
    }
}