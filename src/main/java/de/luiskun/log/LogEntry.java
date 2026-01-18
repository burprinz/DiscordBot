package de.luiskun.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogEntry {

    private static DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm:ss");


    LocalDateTime time;
    int entry_num;
    int type;
    String message;

    public LogEntry(LocalDateTime time, int entry_num, int type, String message) {
        this.time = time;
        this.entry_num = entry_num;
        this.type = type;
        this.message = message;
    }

    public String getMessage() {
        return entry_num + "\t" + time.format(timeFormat) + "\t" + Log.typeStrings[type] + "\t" + message;
    }


}
