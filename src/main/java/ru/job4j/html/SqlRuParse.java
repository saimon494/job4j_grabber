package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Map;

public class SqlRuParse {
    public static LocalDateTime parseDate(String dateStr) {
        LocalDateTime date;
        Map<Long, String> monthNameMap = new HashMap<>();
        monthNameMap.put(1L, "янв");
        monthNameMap.put(2L, "фев");
        monthNameMap.put(3L, "мар");
        monthNameMap.put(4L, "апр");
        monthNameMap.put(5L, "май");
        monthNameMap.put(6L, "июн");
        monthNameMap.put(7L, "июл");
        monthNameMap.put(8L, "авг");
        monthNameMap.put(9L, "сен");
        monthNameMap.put(10L, "окт");
        monthNameMap.put(11L, "ноя");
        monthNameMap.put(12L, "дек");

        DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                .appendPattern("d ")
                .appendText(ChronoField.MONTH_OF_YEAR, monthNameMap)
                .appendPattern(" yy, ")
                .appendPattern("HH:mm")
                .toFormatter();

        int index = dateStr.indexOf(",");
        if (dateStr.contains("сегодня")) {
            dateStr = dateStr.substring(index + 2);
            String[] timeStr = dateStr.split(":");
            int hours = Integer.parseInt(timeStr[0]);
            int minutes = Integer.parseInt(timeStr[1]);
            date = LocalDate.now().atTime(hours, minutes);
        } else if (dateStr.contains("вчера")) {
            dateStr = dateStr.substring(index + 2);
            String[] timeStr = dateStr.split(":");
            int hours = Integer.parseInt(timeStr[0]);
            int minutes = Integer.parseInt(timeStr[1]);
            date = LocalDate.now().atTime(hours, minutes).minusDays(1);
        } else {
            date = LocalDateTime.parse(dateStr, fmt);
        }
        return date;
    }

    public static void parsePage(int page) throws IOException {
        String url = String.format("https://www.sql.ru/forum/job-offers/%d", page);
        Document doc = Jsoup.connect(url).get();
        Elements row = doc.select(".postslisttopic");
        Elements dataRow = doc.select("td[style].altCol");
        for (int i = 0; i < row.size(); i++) {
            Element href = row.get(i).child(0);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
            Element data = dataRow.get(i);
            System.out.println(parseDate(data.text()));
        }
    }

    public static void main(String[] args) throws IOException {
        for (int i = 1; i <= 5; i++) {
            parsePage(i);
        }
    }
}