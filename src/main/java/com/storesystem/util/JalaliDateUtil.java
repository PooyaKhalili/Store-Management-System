package com.storesystem.util;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.PersianCalendar;
import java.util.Date;

public class JalaliDateUtil {

    private JalaliDateUtil() {}

    public static int compareJalaliDates(String date1, String date2) {
        String[] parts1 = date1.split(" ");
        String[] parts2 = date2.split(" ");

        String[] dateParts1 = parts1[0].split("/");
        String[] dateParts2 = parts2[0].split("/");

        int year1 = Integer.parseInt(dateParts1[0]);
        int month1 = Integer.parseInt(dateParts1[1]);
        int day1 = Integer.parseInt(dateParts1[2]);

        int year2 = Integer.parseInt(dateParts2[0]);
        int month2 = Integer.parseInt(dateParts2[1]);
        int day2 = Integer.parseInt(dateParts2[2]);

        if (year1 != year2) return Integer.compare(year1, year2);
        if (month1 != month2) return Integer.compare(month1, month2);
        if (day1 != day2) return Integer.compare(day1, day2);

        if (parts1.length > 1 && parts2.length > 1) {
            String[] timeParts1 = parts1[1].split(":");
            String[] timeParts2 = parts2[1].split(":");

            int hour1 = Integer.parseInt(timeParts1[0]);
            int minute1 = Integer.parseInt(timeParts1[1]);

            int hour2 = Integer.parseInt(timeParts2[0]);
            int minute2 = Integer.parseInt(timeParts2[1]);

            if (hour1 != hour2) return Integer.compare(hour1, hour2);
            return Integer.compare(minute1, minute2);
        }

        return 0; 
    }

    public static String getCurrentJalaliDateTime() {
        Date currentDate = new Date();

        PersianCalendar jalaliCalendar = new PersianCalendar();
        jalaliCalendar.setTime(currentDate);

        int year = jalaliCalendar.get(Calendar.YEAR);
        int month = jalaliCalendar.get(Calendar.MONTH) + 1;
        int day = jalaliCalendar.get(Calendar.DAY_OF_MONTH);
        int hour = jalaliCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = jalaliCalendar.get(Calendar.MINUTE);


        return String.format("%04d/%02d/%02d %02d:%02d", year, month, day, hour, minute);

    }
}