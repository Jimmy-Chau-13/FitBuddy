package app.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class dateHelper {


    public static Date getStartDate(Date datePicker) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(datePicker);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String startDate = df.format(cal.getTime());
        System.out.println("first day of the week: " + startDate);

        try {
            return df.parse(startDate);
        } catch(ParseException e) {
            return null;
        }

    }

    public static Date getEndDate(Date startDate) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.DATE,6);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String endDate = df . format(cal.getTime());
        System.out.println("last day of the week: " + endDate);

        try {
            return df.parse(endDate);
        } catch(ParseException e) {
            return null;
        }
    }

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String currentDate = df.format(cal.getTime());
        System.out.println("Current Date: " + currentDate);
        return currentDate;
    }

    public static Date convertStringToDate(String date) {
        if(date == null || date.isEmpty())
            return null;
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try {
            return df.parse(date);
        } catch(ParseException e) {
            return null;
        }
    }

    public static String convertDateToString(Date date) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        return df.format(date);
    }

    public static int getDayOfTheWeek(String date) {
        Calendar c = Calendar.getInstance();
        c.setTime(convertStringToDate(date));
        return c.get(Calendar.DAY_OF_WEEK);
    }

}
