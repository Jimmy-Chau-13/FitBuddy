package app.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class WorkOut {
    @Column
    private String exercise;
    @Column
    private int sets;
    @Column
    private int reps;
    @Column
    private int weight;

    public WorkOut(String exercise, int sets, int reps, int weight) {

        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkOut workOut = (WorkOut) o;

        if (sets != workOut.sets) return false;
        if (reps != workOut.reps) return false;
        return exercise != null ? exercise.equals(workOut.exercise) : workOut.exercise == null;
    }

    @Override
    public int hashCode() {
        int result = exercise != null ? exercise.hashCode() : 0;
        result = 31 * result + sets;
        result = 31 * result + reps;
        return result;
    }
}
