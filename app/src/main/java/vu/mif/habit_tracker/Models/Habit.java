package vu.mif.habit_tracker.Models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

// This is a Habit table
@Entity(tableName = "Habit")
public class Habit {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int IconID;
    private int colourID;
    private boolean isRepeatble;
    private int repeatNumber;
    private String endDate;
    private int endGoal;
    private boolean isDaily;
    private int dailyGoal;
    private boolean hasNotifications;

//    @ForeignKey
//    private int userID;


    public Habit(String name, int iconID, int colourID, boolean isRepeatble, int repeatNumber, String endDate, int endGoal, boolean isDaily, int dailyGoal, boolean hasNotifications) {
        this.name = name;
        IconID = iconID;
        this.colourID = colourID;
        this.isRepeatble = isRepeatble;
        this.repeatNumber = repeatNumber;
        this.endDate = endDate;
        this.endGoal = endGoal;
        this.isDaily = isDaily;
        this.dailyGoal = dailyGoal;
        this.hasNotifications = hasNotifications;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getIconID() {
        return IconID;
    }

    public int getColourID() {
        return colourID;
    }

    public boolean isRepeatble() {
        return isRepeatble;
    }

    public int getRepeatNumber() {
        return repeatNumber;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getEndGoal() {
        return endGoal;
    }

    public boolean isDaily() {
        return isDaily;
    }

    public int getDailyGoal() {
        return dailyGoal;
    }

    public boolean isHasNotifications() {
        return hasNotifications;
    }
}