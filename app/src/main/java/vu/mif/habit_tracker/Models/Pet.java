package vu.mif.habit_tracker.Models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;

//This is a pet table
@Entity(tableName = "Pet")
public class Pet {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String pet_name;
    private int aksesuaras;


    //for FireBase
    @Ignore
    public Pet(){}

    public Pet(String pet_name, int aksesuaras) {
        this.pet_name = pet_name;
        this.aksesuaras = aksesuaras;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Exclude
    public int getId() {return id;}

    @Exclude
    public String getPet_name() {
        return pet_name;
    }

    public int getAksesuaras() {
        return aksesuaras;
    }
}
