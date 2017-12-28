package app.event;

import app.util.DateHelper;
import app.workout.WorkOut;
import app.workout.WorkOutController;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventController {

    private static Gson gson = new Gson();

    public static String getAllWorkoutEvent(String userId) {

        List<WorkOut> workout_list = WorkOutController.getListOfAllWorkouts(userId);
        List<Event> event_list = new ArrayList<>();
        if (workout_list.size() == 0) return gson.toJson(event_list);

        int numberOfWorkout = 0;
        Date prevDate = workout_list.get(0).getDate();
        for(int i = 0; i < workout_list.size(); i++) {
            WorkOut currWorkout = workout_list.get(i);
            Date currDate = currWorkout.getDate();

            if(!currDate.equals(prevDate)) {
                String title;
                if (numberOfWorkout == 1)  title =  numberOfWorkout + " workout";
                else title =  numberOfWorkout + " workouts";
                event_list.add(new Event(title,  DateHelper.dateToEventDateString(prevDate)
                        + " workout", DateHelper.dateToEventDateString(prevDate)));
                numberOfWorkout = 1;
                prevDate = currDate;

            }

            else {
                numberOfWorkout++;
            }
        }
        String title;
        if (numberOfWorkout == 1)  title =  numberOfWorkout + " workout";
        else title =  numberOfWorkout + " workouts";
        event_list.add(new Event(title,  DateHelper.dateToEventDateString(prevDate)
                + " workout", DateHelper.dateToEventDateString(prevDate)));
        return gson.toJson(event_list);
    }
}
