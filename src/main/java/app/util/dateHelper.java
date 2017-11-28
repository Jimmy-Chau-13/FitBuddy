package app.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    /*
     * @format == 0 then MM/dd/yyyy
     * @format == 1 then yyyy-MM-dd
     */
    public static Date convertStringToDate(String date, int format) {
        if(date == null || date.isEmpty())
            return null;
        DateFormat df = new SimpleDateFormat();
        if(format == 0)
             df = new SimpleDateFormat("MM/dd/yyyy");
        if(format == 1)
             df = new SimpleDateFormat("yyyy-MM-dd");

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


    public static String dayToString(int value) {
        String day = "";
        switch (value) {
            case 1:
                day = "Sunday";
                break;
            case 2:
                day = "Monday";
                break;
            case 3:
                day = "Tuesday";
                break;
            case 4:
                day = "Wednesday";
                break;
            case 5:
                day = "Thursday";
                break;
            case 6:
                day = "Friday";
                break;
            case 7:
                day = "Saturday";
                break;
        }
        return day;
    }

    public static Date getStartDate(Date datePicker, int previous) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(datePicker);
        cal.add(Calendar.DATE,-previous);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String startDate = df . format(cal.getTime());
        System.out.println("Start Date " + startDate);

        try {
            return df.parse(startDate);
        } catch(ParseException e) {
            return null;
        }
    }

}
