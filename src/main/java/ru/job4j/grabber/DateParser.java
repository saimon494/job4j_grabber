package ru.job4j.grabber;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DateParser {
    private LocalDateTime date;
    private final List<Long> keys = Arrays.asList(
            1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L);

    private final List<String> values = Arrays.asList(
            "янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек");

    private final Map<Long, String> monthNameMap = IntStream.range(0, keys.size())
            .boxed()
            .collect(Collectors.toMap(keys::get, values::get));

    public LocalDateTime parseDate(String dateStr) {

        DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                .appendPattern("d ")
                .appendText(ChronoField.MONTH_OF_YEAR, monthNameMap)
                .appendPattern(" yy, ")
                .appendPattern("HH:mm")
                .toFormatter();

        int index = dateStr.indexOf(",");
        if (dateStr.contains("сегодня")) {
            dateStr = dateStr.substring(index + 2);
            date = LocalDate.now().atTime(LocalTime.parse(dateStr));
        } else if (dateStr.contains("вчера")) {
            dateStr = dateStr.substring(index + 2);
            date = LocalDate.now().atTime(LocalTime.parse(dateStr)).minusDays(1);
        } else {
            date = LocalDateTime.parse(dateStr, fmt);
        }
        return date;
    }
}