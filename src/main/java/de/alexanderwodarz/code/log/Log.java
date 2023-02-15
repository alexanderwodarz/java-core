package de.alexanderwodarz.code.log;

import de.alexanderwodarz.code.JavaCore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

public class Log {

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
    public static Consumer<String> print;

    public static void log(Object log) {
        log(log, Level.UNKNOWN);
    }

    public static void log(Object log, Level level) {
        log(log, level.getLevel(), level == Level.ERROR);
    }

    public static void log(Object log, String type, boolean error) {
        JavaCore.getOriginalStream().printf(
                "%-32s %-25s %s%n",
                Color.BRIGHT_BLACK + format.format(new Date()) + Color.RESET,
                "|" + type + Color.RESET,
                "|" + (error ? Color.RED : "") + log + Color.RESET
        );
        if(print != null)
            print.accept(Color.BRIGHT_BLACK + format.format(new Date()) + Color.RESET + "|" + type + Color.RESET + "|" + (error ? Color.RED : "") + log + Color.RESET);
    }

}
