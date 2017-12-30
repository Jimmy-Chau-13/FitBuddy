package app.util;

import com.google.gson.JsonPrimitive;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

public class DateHelper {

    public static Date jsonToDate(JsonPrimitive json_date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String date_string = json_date.getAsString();
        try {
            return formatter.parse(date_string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToEventDateString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    //    MM/dd/YYYY to Date
    public static Date stringToDate(String date_string) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        try {
            return formatter.parse(date_string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date stringToDate2(String date_string) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        try {
            return formatter.parse(date_string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(date);
    }

    public static Date getFirstDateOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal.getTime();
    }

    public static Date getLastDateOfMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal.getTime();
    }

    public static boolean checkMonthYearIsCurrent(Date now, Date toCheck) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMMM yyyy");
        if(formatter.format(now).equals(formatter.format(toCheck))) return true;
        else return false;
    }

    public static String dateToMonthYear(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMMMM yyyy");
        return formatter.format(date);
    }

    public static int getDayFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

}
