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
    private String iconID;
    private int colourID;
    private boolean isRepeatble;
    private int repeatNumber;
    private int endYear;
    private int endMonth;
    private int endDayOfMonth;
    private int totalProgress;
    private int currentProgress;

    public Habit(String name, String iconID, int colourID, boolean isRepeatble, int repeatNumber, int endYear, int endMonth, int endDayOfMonth, int totalProgress, int currentProgress) {
        this.name = name;
        this.iconID = iconID;
        this.colourID = colourID;
        this.isRepeatble = isRepeatble;
        this.repeatNumber = repeatNumber;
        this.endYear = endYear;
        this.endMonth = endMonth;
        this.endDayOfMonth = endDayOfMonth;
        this.totalProgress = totalProgress;
        this.currentProgress = currentProgress;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIconID() {
        return iconID;
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

    public int getTotalProgress() {
        return totalProgress;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public int getEndMonth() {
        return endMonth;
    }

    public void setEndMonth(int endMonth) {
        this.endMonth = endMonth;
    }

    public int getEndDayOfMonth() {
        return endDayOfMonth;
    }

    public void setEndDayOfMonth(int endDayOfMonth) {
        this.endDayOfMonth = endDayOfMonth;
    }
}
