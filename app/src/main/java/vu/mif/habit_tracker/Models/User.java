package vu.mif.habit_tracker.Models;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

//This is a User table
@Entity(tableName = "User")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String username;
    private int currency;
    private String pictureURL;

//    @ForeignKey
////    private int Pet_id;


    public User(String username, int currency, String pictureURL) {
        this.username = username;
        this.currency = currency;
        this.pictureURL = pictureURL;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() { return id; }

    public String getUsername() {
        return username;
    }

    public int getCurrency() {
        return currency;
    }

    public String getPictureURL() {
        return pictureURL;
    }
}
