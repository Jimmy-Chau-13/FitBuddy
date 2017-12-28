package app.util;

import com.google.gson.JsonPrimitive;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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

    public static String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(date);
    }

}
