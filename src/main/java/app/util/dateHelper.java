package app.util;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


// LocalDate is in the format of yyyy-MM-dd
public class DateHelper {

    private static DateTimeFormatter df = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public static LocalDate convertStringToLocalDate(String stringDate) {
       return LocalDate.parse(stringDate,df);
    }

    public static String convertLocalDateToString(LocalDate localdate) {
        return localdate.format(df);
    }



}
