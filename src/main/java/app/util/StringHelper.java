package app.util;

import java.util.Comparator;

public class StringHelper {

    public static int[] stringToArray(String toArray, int sets) {
        int[] array = new int[sets];
        String[] s = toArray.split(",");
        for (int i = 0; i < sets; i++) {
            array[i] = Integer.parseInt(s[i]);
        }
        return array;
    }

    public static String printArray(int[] toPrint) {
        String s ="";
        for (int i = 0; i < toPrint.length; i++) {
            s += toPrint[i] + ", ";
        }
        return s;
    }

    public static String ArrayToString(int[] array) {
        String s ="[";
        for (int i = 0; i < array.length; i++) {
            if (i == array.length -1)
                s += array[i] + "]";
            else
                s += array[i] + ", ";
        }
        return s;
    }




}
