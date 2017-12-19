package app.workout;


import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

public class SuperSet {
    @Id
    private ObjectId id;
    WorkOut[] exercises;
    int sets;
    private String userId;
    private String date;



    public SuperSet(WorkOut[] exercises, int sets) {
        this.exercises = exercises;
        this.sets = sets;
    }

    public void setId(String id) {
        if(id != null && !id.isEmpty()) {
            this.id = new ObjectId(id);
        }
    }

    public ObjectId getId() { return id; }

    public WorkOut[] getExercises() {
        return exercises;
    }

    public void setExercises(WorkOut[] exercises) {
        this.exercises = exercises;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
