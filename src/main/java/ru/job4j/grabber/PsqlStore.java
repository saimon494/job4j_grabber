package ru.job4j.grabber;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private final Connection cn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
            cn = DriverManager.getConnection(
                    cfg.getProperty("jdbc.url"),
                    cfg.getProperty("jdbc.username"),
                    cfg.getProperty("jdbc.password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(Post post) throws SQLException {
        String sql = "insert into post(name, description, link, created) values (?, ?, ?, ?)";
        try (PreparedStatement statement =
                     cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, post.getName());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getLink());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreateDate()));
            statement.execute();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public List<Post> getAll() throws SQLException {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement =
                     cn.prepareStatement("select * from post")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(new Post(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("link"),
                            resultSet.getTimestamp("created").toLocalDateTime()
                    ));
                }
            }
        }
        return posts;
    }

    @Override
    public Post finById(String id) throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "select * from post where id = ?";
        try (PreparedStatement statement =
                cn.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(id));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    posts.add(new Post(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("link"),
                            resultSet.getTimestamp("created").toLocalDateTime()
                    ));
                }
            }
        }
        return posts.size() > 0 ? posts.get(0) : null;
    }

    @Override
    public void close() throws Exception {
        if (cn != null) {
            cn.close();
        }
    }

    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        try (InputStream in = PsqlStore.class
        .getClassLoader()
        .getResourceAsStream("app.properties")) {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SqlRuParse sqlRuParse = new SqlRuParse();
        try {
            String link1 = "https://www.sql.ru/forum/1333574/system-administrator-linux-remote";
            String link2 = "https://www.sql.ru/forum/1333465/devops-engineer";
            Post post1 = sqlRuParse.detail(link1);
            Post post2 = sqlRuParse.detail(link2);
            System.out.println(post1);
            System.out.println(post2);
            PsqlStore psqlStore = new PsqlStore(properties);
            psqlStore.save(post1);
            psqlStore.save(post2);

            Post findPost1 = psqlStore.finById(String.valueOf(post1.getId()));
            System.out.println(findPost1.getName());
            Post findPost2 = psqlStore.finById(String.valueOf(post2.getId()));
            System.out.println(findPost2.getName());

            List<Post> posts = psqlStore.getAll();
            for (int i = 0; i < posts.size(); i++) {
                System.out.println(posts.get(i).getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
