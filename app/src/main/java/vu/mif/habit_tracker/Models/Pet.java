package vu.mif.habit_tracker.Models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

//This is a pet table
@Entity(tableName = "Pet")
public class Pet {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String pet_name;
    private int aksesuaras;



    public Pet(String pet_name, int aksesuaras) {
        this.pet_name = pet_name;
        this.aksesuaras = aksesuaras;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {return id;}

    public String getPet_name() {
        return pet_name;
    }

    public int getAksesuaras() {
        return aksesuaras;
    }
}
