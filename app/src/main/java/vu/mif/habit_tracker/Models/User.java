package vu.mif.habit_tracker.Models;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;

//This is a User table
@Entity(tableName = "User")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;


    private String username;
    private int currency;
    private String pictureURL;
    private String UID;

    //For FireBase
    @Ignore
    public User(){}

    public User(String username, int currency, String pictureURL, String UID) {
        this.username = username;
        this.currency = currency;
        this.pictureURL = pictureURL;
        this.UID = UID;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Exclude
    public int getId() { return id; }

    public String getUsername() {
        return username;
    }

    public int getCurrency() {
        return currency;
    }

    @Exclude
    public String getPictureURL() {
        return pictureURL;
    }

    @Exclude
    public String getUID() {return UID;}



}
