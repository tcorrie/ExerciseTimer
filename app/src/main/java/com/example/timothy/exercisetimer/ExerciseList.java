package com.example.timothy.exercisetimer;
import android.support.annotation.NonNull;
public class ExerciseList {// Defines the data of an exercise
    private String name;
    private final int timeon;
    private final int timeoff;
    private final int rest;
    private final int sides;
    private final int reps;
    private final int sets;
    //constructors
    public ExerciseList(String name, int timeon, int timeoff, int rest, int sides, int reps, int sets) {
        this.name = name;
        this.timeon = timeon;
        this.timeoff = timeoff;
        this.rest = rest;
        this.sides = sides;
        this.reps = reps;
        this.sets = sets;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getTimeon() {
        return timeon;
    }
    //public void setTimeon(int timeon) {this.timeon = timeon;} Never used. Will keep here anyway.
    public int getTimeoff() {
        return timeoff;
    }
    //public void setTimeoff(int timeoff) {this.timeoff = timeoff;} Never used. Will keep here anyway.
    public int getRest() {return rest;}
    //public void setRest(int rest) {this.rest = rest;} Never used. Will keep here anyway.
    public int getSides() {return sides;}
    //public void setSides(int sides) {this.sides = sides;} Never used. Will keep here anyway.
    public int getReps() {return reps;}
    //public void setReps(int reps) {this.reps = reps;} Never used. Will keep here anyway.
    public int getSets() {return sets;}
    //public void setSets(int sets) {this.sets = sets;} Never used. Will keep here anyway.
    @NonNull
    @Override
    public String toString() {//Fancy print of an exercise's data. Is it ever used?
        return "ExerciseList{" +
                "name='" + name + '\'' +
                ", timeon=" + timeon +
                ", timeoff=" + timeoff +
                ", rest=" + rest +
                ", sides=" + sides +
                ", reps=" + reps +
                ", sets=" + sets +
                '}';
    }
}
