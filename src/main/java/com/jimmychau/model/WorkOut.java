package com.jimmychau.model;

public class WorkOut {
    private String exercise;
    private int sets;
    private int reps;

    public WorkOut(String exercise, int sets, int reps) {
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
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
