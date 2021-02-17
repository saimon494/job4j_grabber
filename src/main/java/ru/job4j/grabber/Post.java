package ru.job4j.grabber;

import java.time.LocalDateTime;

public class Post {
    private String name;
    private String description;
    private String link;
    private LocalDateTime createDate;

    public Post(String name, String description, String link, LocalDateTime createDate) {
        this.name = name;
        this.description = description;
        this.link = link;
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "\nPost {"
                + "name = '" + name + '\''
                + ",\n description = '" + description + '\''
                + ",\n link = '" + link + '\''
                + ",\n createDate = " + createDate
                + '}';
    }
}