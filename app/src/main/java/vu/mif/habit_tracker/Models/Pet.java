package vu.mif.habit_tracker.Models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

//This is a pet table
@Entity(tableName = "pet")
public class Pet {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String pet_name;
    private int Aksesuaras;

//    @ForeignKey
//    private int userID;


    public Pet(String pet_name, int aksesuaras) {
        this.pet_name = pet_name;
        Aksesuaras = aksesuaras;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPet_name() {
        return pet_name;
    }

    public int getAksesuaras() {
        return Aksesuaras;
    }
}
