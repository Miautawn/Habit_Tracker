package vu.mif.habit_tracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import vu.mif.habit_tracker.Models.Pet;
import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.Repositories.PetRepository;
import vu.mif.habit_tracker.Repositories.UserRepository;

public class PetTestViewModel extends AndroidViewModel {
    private PetRepository repo;
    private LiveData<Pet> pet;
    public PetTestViewModel(@NonNull Application application) {
        super(application);
        repo = new PetRepository(application);
        pet = repo.getPet();
    }

    public void insertPet(Pet pet)
    {
        //to prevent duplication
        if(this.pet.getValue() == null) repo.insertPet(pet);
    }
    public void updatePet(Pet pet)
    {
        repo.updatePet(pet);
    }
    public void deletePet()
    {
        repo.deletePet();
    }
    public LiveData<Pet> getPet()
    {
        return pet;
    }
}
