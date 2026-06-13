package com.storesystem.util;

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
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        int gYear = now.getYear();
        int gMonth = now.getMonthValue();
        int gDay = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();

        int gy = gYear - 1600;
        int gm = gMonth - 1;
        int gd = gDay - 1;

        int g_day_no = 365 * gy + (gy + 4) / 4 - (gy + 100) / 100 + (gy + 400) / 400;
        int[] g_days_in_ctrl = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        for (int i = 0; i < gm; ++i) g_day_no += g_days_in_ctrl[i + 1];
        if (gm > 1 && ((gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0))) g_day_no++;
        g_day_no += gd;

        int j_day_no = g_day_no - 79;
        int j_np = j_day_no / 12053;
        j_day_no %= 12053;

        int jy = 979 + 33 * j_np + 4 * (j_day_no / 1461);
        j_day_no %= 1461;

        if (j_day_no >= 366) {
            jy += (j_day_no - 1) / 365;
            j_day_no = (j_day_no - 1) % 365;
        }

        int[] j_days_in_ctrl = {0, 31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29};
        int jm = 0;
        for (int i = 0; i < 12; ++i) {
            if (j_day_no < j_days_in_ctrl[i + 1]) {
                jm = i + 1;
                break;
            }
            j_day_no -= j_days_in_ctrl[i + 1];
        }
        int jd = j_day_no + 1;

        return String.format("%04d/%02d/%02d %02d:%02d", jy, jm, jd, hour, minute);
    }
}