package de.luiskun.log;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Log {

    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final int FILE_SIZE = 100;


    public static final int DEBUG = 0;
    public static final int INFO = 1;
    public static final int SUCCESS = 2;
    public static final int ERROR = 3;
    public static final int DISCORD = 4;

    public static final Color[] typeColors = {
            Color.decode("#4fc3f7"),
            Color.decode("#81c784"),
            Color.decode("#66e2b3"),
            Color.decode("#ef5350"),
            Color.decode("#ffa3dd")
    };

    public static final String[] typeStrings = {
            "Debug",
            "Info",
            "Success",
            "Error",
            "Discord"
    };

    private static final Object LOCK = new Object();

    private static PrintWriter consoleWriter;

    private static List<LogEntry> logEntries;

    public static Font logFont;
    private static LogPanel logScreen;

    public static void start() {
        synchronized (LOCK) {
            logEntries = new ArrayList<>();
            try {
                logFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/main/resources/fonts/console_font.ttf"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame();
            logScreen = new LogPanel();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(logScreen);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    public static void debug(String msg) {
        synchronized (LOCK) {
            LogEntry entry = new LogEntry(LocalDateTime.now(), logEntries.size(), DEBUG, msg);
            logEntries.add(entry);
            logScreen.updateLogs(logEntries);
            if (logEntries.size() > FILE_SIZE) {
                save();
            }
        }
    }

    public static void info(String msg) {
        synchronized (LOCK) {
            LogEntry entry = new LogEntry(LocalDateTime.now(), logEntries.size(), INFO, msg);
            logEntries.add(entry);
            logScreen.updateLogs(logEntries);
            if (logEntries.size() > FILE_SIZE) {
                save();
            }
        }
    }

    public static void success(String msg) {
        synchronized (LOCK) {
            LogEntry entry = new LogEntry(LocalDateTime.now(), logEntries.size(), SUCCESS, msg);
            logEntries.add(entry);
            logScreen.updateLogs(logEntries);
            if (logEntries.size() > FILE_SIZE) {
                save();
            }
        }
    }

    public static void error(String msg) {
        synchronized (LOCK) {
            LogEntry entry = new LogEntry(LocalDateTime.now(), logEntries.size(), ERROR, msg);
            logEntries.add(entry);
            logScreen.updateLogs(logEntries);
            if (logEntries.size() > FILE_SIZE) {
                save();
            }
        }
    }

    public static void discord(String msg) {
        synchronized (LOCK) {
            LogEntry entry = new LogEntry(LocalDateTime.now(), logEntries.size(), DISCORD, msg);
            logEntries.add(entry);
            logScreen.updateLogs(logEntries);
            if (logEntries.size() > FILE_SIZE) {
                save();
            }
        }
    }

    public static void save() {

        synchronized (LOCK) {
            String current_dir = "src/main/resources/logs/" + LocalDate.now().format(timeFormat);
            Path current_dir_path = Paths.get(current_dir);
            String finalPath;
            int amEntries = logEntries.size();

            try {
                if (!Files.exists(current_dir_path)) {
                    Files.createDirectory(current_dir_path);
                }
                int num = 0;
                String filename = getFilename(new File(current_dir).listFiles().length) + ".txt";
                finalPath = current_dir + "/" + filename;

                BufferedWriter bw = new BufferedWriter(new FileWriter(finalPath, true));
                for (int i = 0; i < amEntries; i++) {
                    bw.write(logEntries.get(i).getMessage());
                    bw.newLine();
                }
                bw.close();

                logEntries.clear();
                success("Saved " + amEntries + " Log-Entries to <" + finalPath + ">");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getFilename(int num) {
        String s = ""+num;
        String result = "";
        for (int i = 0; i < 3-s.length(); i++) {
            result += "0";
        }
        return result + s;
    }

}
