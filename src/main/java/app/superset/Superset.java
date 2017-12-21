package app.superset;

import app.workout.WorkOut;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

import java.util.Arrays;

public class Superset {

    @Id
    private ObjectId id;
    private String userId;
    private WorkOut[] workouts;
    private String date;

    public  Superset() {}

    public Superset(WorkOut[] workouts, String date ) {
        this.date = date;
        this.workouts = workouts;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(String id) {
        if(id != null && !id.isEmpty()) {
            this.id = new ObjectId(id);
        }
    }

    public WorkOut[] getWorkouts() {
        return workouts;
    }

    public void setWorkouts(WorkOut[] workouts) {
        this.workouts = workouts;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Superset{" +
                "workouts=" + Arrays.toString(workouts) +
                ", date='" + date + '\'' +
                '}';
    }
}

