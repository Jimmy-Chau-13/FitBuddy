package app.workout;

import org.junit.Test;

import static org.junit.Assert.*;

public class WorkOutTest {


    @Test
    public void compareToTest() throws Exception {
        int[] arr = {40,40,40};
        WorkOut workout1 = new WorkOut("bench", 3, arr, arr, "12/20/2017" );
        WorkOut workout2 = new WorkOut("bench", 3, arr, arr, "12/21/2017" );
        WorkOut workout3 = new WorkOut("bench", 3, arr, arr, "12/21/2018" );
        WorkOut workout4 = new WorkOut("bench", 3, arr, arr, "01/21/2018" );

        assertEquals(-1, workout2.compareTo(workout1) );
        assertEquals(1, workout1.compareTo(workout2));
        assertEquals(-1, workout3.compareTo(workout2));
        assertEquals(-11, workout3.compareTo(workout4));
    }


}