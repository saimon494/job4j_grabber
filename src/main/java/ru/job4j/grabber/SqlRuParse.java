package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {
    private static DateParser dateParser = new DateParser();

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> posts = new ArrayList<>();
        for (int j = 1; j <= 5; j++) {
            link = String.format(link + "/%d", j);
            Document doc = Jsoup.connect(link).get();
            Elements row = doc.select(".postslisttopic");
            for (int i = 0; i < row.size(); i++) {
                Element href = row.get(i).child(0);
                if (!row.get(i).text().contains("Важно")) {
                    posts.add(detail(href.attr("href")));
                }
            }
            link = link.substring(0, link.length() - 2);
        }
        return posts;
    }

    @Override
    public Post detail(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        Element msgHeader = doc.selectFirst(".messageHeader");
        String name = msgHeader.text().replace("[new]", "").trim();
        Element text = doc.select(".msgBody").get(1);
        String description = text.text();
        Element dataText = doc.selectFirst(".msgFooter");
        LocalDateTime createdDate = dateParser.parseDate(dataText.text()
                .substring(0, dataText.text().indexOf("[") - 1));
        return new Post(name, description, link, createdDate);
    }

    public static void main(String[] args) throws IOException {
        SqlRuParse sqlRuParse = new SqlRuParse();
        String url = "https://www.sql.ru/forum/job-offers";
        List<Post> posts = sqlRuParse.list(url);
        System.out.println(posts);
    }
}
