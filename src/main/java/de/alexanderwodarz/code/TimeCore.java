package de.alexanderwodarz.code;

import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class TimeCore {

    private Date date;
    private TimeZone zone;

    @SneakyThrows
    public TimeCore(String format, String timestamp) {
        date =  new SimpleDateFormat(format).parse(timestamp);
        zone = TimeZone.getTimeZone("Europe/Berlin");
    }

    public TimeCore() {
        this(System.currentTimeMillis());
    }

    public TimeCore(long timestamp) {
        this(timestamp, "Europe/Berlin");
    }

    public TimeCore(long timestamp, String timezone) {
        date = new Date(timestamp);
        zone = TimeZone.getTimeZone(timezone);
    }

    public TimeCore(Date date) {
        this(date, "Europe/Berlin");
    }

    public TimeCore(Date date, String timezone) {
        this.date = date;
        zone = TimeZone.getTimeZone(timezone);
    }

    public Date getDate() {
        return date;
    }

    public String addYear(int year) {
        return modify(true, year, Calendar.YEAR);
    }

    public String addMonth(int month) {
        return modify(true, month, Calendar.MONTH);
    }

    public String addWeek(int week) {
        return modify(true, week * 7, Calendar.DATE);
    }

    public String addDay(int day) {
        return modify(true, day, Calendar.DATE);
    }

    public String addHour(int hour) {
        return modify(true, hour, Calendar.HOUR);
    }

    public String addMinute(int minute) {
        return modify(true, minute, Calendar.MINUTE);
    }

    public String addSecond(int second) {
        return modify(true, second, Calendar.SECOND);
    }

    public String removeYear(int year) {
        return modify(false, year, Calendar.YEAR);
    }

    public String removeMonth(int month) {
        return modify(false, month, Calendar.MONTH);
    }

    public String removeWeek(int week) {
        return modify(false, week * 7, Calendar.DATE);
    }

    public String removeDay(int day) {
        return modify(false, day, Calendar.DATE);
    }

    public String removeHour(int hour) {
        return modify(false, hour, Calendar.HOUR);
    }

    public String removeMinute(int minute) {
        return modify(false, minute, Calendar.MINUTE);
    }

    public String removeSecond(int second) {
        return modify(false, second, Calendar.SECOND);
    }

    public void setSecond(int second) {
        date.setSeconds(second);
    }

    public void setMinute(int minute) {
        date.setMinutes(minute);
    }

    public void setHour(int hour) {
        date.setHours(hour);
    }

    public String getLocalString() {
        Calendar calendar = Calendar.getInstance(zone);
        calendar.setTime(date);
        return calendar.getTime().toLocaleString();
    }

    @SneakyThrows
    private String modify(boolean add, int value, int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (add)
            calendar.add(type, value);
        else
            calendar.add(type, value / -1);
        date = calendar.getTime();
        return calendar.getTime().toLocaleString();
    }

}
