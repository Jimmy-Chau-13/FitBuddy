package app.workout;


import app.util.StringHelper;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class WorkOut implements Comparable<WorkOut> {

    @Id
    private ObjectId id;
    private String exercise;
    private int sets;
    private int[] reps;
    private int[] weight;
    private String userId;
    private String date;

    public WorkOut() { }

    public WorkOut(String exercise, int sets, int[] reps, int[] weight, String date) {
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.date = date;
    }

    public void setId(String id) {
        if(id != null && !id.isEmpty()) {
            this.id = new ObjectId(id);
        }
    }

    public ObjectId getId() { return id; }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public String getExercise() {
        return exercise;
    }

    public int getSets() {
        return sets;
    }

    public int[] getReps() {
        return reps;
    }

    public int[] getWeight() {
        return weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAverage() {
        int total = 0;
        int total_reps = 0;
        for(int i = 0; i < sets; i++) {
            total += (reps[i] * weight[i]);
            total_reps += reps[i];
        }
        return total / total_reps ;
    }

    // Sort by most recent date
    @Override
    public int compareTo(WorkOut workout) {
        int compareYear = Integer.parseInt(workout.getDate().substring(6,10));
        int year = Integer.parseInt(this.getDate().substring(6,10));
        if(year == compareYear) {
            int compareMonth = Integer.parseInt(workout.getDate().substring(0,2));
            int month = Integer.parseInt(this.getDate().substring(0,2));
            if(month == compareMonth) {
                int compareDay = Integer.parseInt(workout.getDate().substring(3,5));
                int day = Integer.parseInt(this.getDate().substring(3,5));
                return compareDay - day;
            }
            else return compareMonth - month;
        }

        else return compareYear - year;
    }

    public String toJson() {

        return  "{" +
                "\"id\": " + "\"" + getId() + "\", " +
                "\"exercise\":" + "\"" + getExercise() + "\", " +
                "\"sets\":" + "\"" + getSets() + "\", " +
                "\"reps\":" + "\"" + StringHelper.ArrayToString(getReps()) + "\", " +
                "\"weight\":" + "\"" + StringHelper.ArrayToString(getWeight()) + "\" " +
                "}" ;
    }

}
