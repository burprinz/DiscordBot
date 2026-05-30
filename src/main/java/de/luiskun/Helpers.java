package de.luiskun;

import java.util.Locale;

public class Helpers {

    public static String getTimeString(int seconds) {
        long days    = seconds / 86400;
        long hours   = (seconds % 86400) / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs    = seconds % 60;

        return String.format("%02d",days)+"d"+String.format("%02d",hours)+"h"+String.format("%02d",minutes)+"m"+String.format("%02d",secs)+"s";
    }

    public static String addIntegerPoints(int value) {
        return String.format(Locale.GERMANY, "%,d",value);
    }

}
