package app.model;


import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

public class WorkOut {

    @Id
    private ObjectId id;
    private String exercise;
    private int sets;
    private int reps;
    private int weight;
    private String userId;

    public WorkOut() {

    }

    public WorkOut(String exercise, int sets, int reps, int weight) {

        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
    }

    public void setId(String id) {
        if(id != null && !id.isEmpty()) {
            this.id = new ObjectId(id);
        }
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }



}
