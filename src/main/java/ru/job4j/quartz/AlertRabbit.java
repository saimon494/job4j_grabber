package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {
    public static void main(String[] args) throws Exception {
        Properties config = new Properties();
        try (InputStream in = AlertRabbit.class.
                getClassLoader().
                getResourceAsStream("rabbit.properties")
        ) {
            config.load(in);
        }
        int interval = Integer.parseInt(config.getProperty("rabbit.interval"));
        Class.forName(config.getProperty("jdbc.driver"));
        String url = config.getProperty("jdbc.url");
        String login = config.getProperty("jdbc.username");
        String password = config.getProperty("jdbc.password");
        try (Connection connection = DriverManager.getConnection(url, login, password)) {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap dataMap = new JobDataMap();
            dataMap.put("connection", connection);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(dataMap)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(interval)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        }
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            Connection connection = (Connection) context.getJobDetail()
                    .getJobDataMap()
                    .get("connection");
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 "insert into rabbit(created_date) values (?)")) {
                statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}