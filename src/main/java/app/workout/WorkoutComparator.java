package app.workout;

import java.util.Comparator;

public class WorkoutComparator {

    public static class SortByDate implements Comparator<WorkOut> {
        public int compare(WorkOut w1, WorkOut w2) {
            return w1.compareTo(w2);
        }
    }
}
