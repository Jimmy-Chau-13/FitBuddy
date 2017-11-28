package app.workout;


import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WorkOut {

    @Id
    private ObjectId id;
    private String exercise;
    private int sets;
    private int reps;
    private int weight;
    private String userId;
    private String rowId;
    private Date date;

    public WorkOut() { }

    public WorkOut(String exercise, int sets, int reps, int weight, Date date) {
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

    public int getReps() {
        return reps;
    }

    public int getWeight() {
        return weight;
    }

    public void setRowId() {
        rowId = id.toString();
    }

    public String getRowId() {
        return rowId;
    }

    public String getDate() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String workoutDate = df.format(date);
        return workoutDate;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String toJson() {

        return  "{" +
                "\"id\": " + "\"" + getId() + "\", " +
                "\"Exercise\":" + "\"" + getExercise() + "\", " +
                "\"Sets\":" + "\"" + getSets() + "\", " +
                "\"Reps\":" + "\"" + getReps() + "\", " +
                "\"Weight\":" + "\"" + getWeight() + "\" " +
                "}" ;
    }

}