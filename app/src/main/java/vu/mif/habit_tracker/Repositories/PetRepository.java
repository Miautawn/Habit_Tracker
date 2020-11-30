package vu.mif.habit_tracker.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import vu.mif.habit_tracker.DAOs.petDAO;
import vu.mif.habit_tracker.DAOs.petDAO;
import vu.mif.habit_tracker.Models.Pet;
import vu.mif.habit_tracker.Models.Pet;
import vu.mif.habit_tracker.Models.Pet;
import vu.mif.habit_tracker.roomDB;

public class PetRepository {
    private petDAO petDAO;
    private LiveData<Pet> pet;

    public PetRepository(Application application)
    {
        roomDB database = roomDB.getInstance(application);
        this.petDAO = database.getPetDAO();
        this.pet = petDAO.getPet();
    }

    // These methods are the only thing that view model will see and they have such shit structure because they need to be run on a background task
    public void insertPet(Pet Pet) { new PetRepository.InsertPetAsyncTask(petDAO).execute(Pet); }
    public void updatePet(Pet Pet) { new PetRepository.UpdatePetAsyncTask(petDAO).execute(Pet); }
    public void deletePet() { new PetRepository.DeletePetTask(petDAO).execute(); }
    public LiveData<Pet> getPet() { return pet; }

    private static class InsertPetAsyncTask extends AsyncTask<Pet, Void, Void>
    {
        private vu.mif.habit_tracker.DAOs.petDAO petDAO;
        private InsertPetAsyncTask(petDAO petDAO)
        {
            this.petDAO = petDAO;
        }

        @Override
        protected Void doInBackground(Pet... Pet) {
            petDAO.insert(Pet[0]);
            return null;
        }
    }
    private static class UpdatePetAsyncTask extends AsyncTask<Pet, Void, Void>
    {
        private petDAO petDAO;
        private UpdatePetAsyncTask(petDAO petDAO)
        {
            this.petDAO = petDAO;
        }

        @Override
        protected Void doInBackground(Pet... Pet) {
            petDAO.update(Pet[0]);
            return null;
        }
    }
    private static class DeletePetTask extends AsyncTask<Void, Void, Void>
    {
        private petDAO petDAO;
        private DeletePetTask(petDAO petDAO)
        {
            this.petDAO = petDAO;
        }

        @Override
        protected Void doInBackground(Void... Pet) {
            petDAO.deletePet();
            return null;
        }
    }


}
