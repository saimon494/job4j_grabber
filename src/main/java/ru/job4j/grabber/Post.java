package ru.job4j.grabber;

import java.time.LocalDateTime;

public class Post {
    private String name;
    private String description;
    private String link;
    private LocalDateTime date;

    public Post(String name, String description, String link, LocalDateTime date) {
        this.name = name;
        this.description = description;
        this.link = link;
        this.date = date;
    }
}