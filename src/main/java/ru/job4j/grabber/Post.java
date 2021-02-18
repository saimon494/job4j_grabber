package ru.job4j.grabber;

import java.time.LocalDateTime;

public class Post {
    private int id;
    private final String name;
    private final String description;
    private final String link;
    private final LocalDateTime createDate;

    public Post(String name, String description, String link, LocalDateTime createDate) {
        this.name = name;
        this.description = description;
        this.link = link;
        this.createDate = createDate;
    }

    public Post(int id, String name, String description, String link, LocalDateTime createDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.link = link;
        this.createDate = createDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
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