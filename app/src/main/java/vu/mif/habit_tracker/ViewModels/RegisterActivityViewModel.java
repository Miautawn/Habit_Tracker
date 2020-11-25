package vu.mif.habit_tracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.Repositories.RegisterRepository;

//This is our viewmodel, which is designed for the registerActivity
public class RegisterActivityViewModel extends AndroidViewModel {
    private RegisterRepository repo;
    private LiveData<List<User>> users;

    public RegisterActivityViewModel(@NonNull Application application) {
        super(application);
        repo = new RegisterRepository(application);
        users = repo.getAllUsers();
    }

    public void insertUser(User user)
    {
        repo.insertUser(user);
    }
    public void updateUser(User user)
    {
        repo.updateUser(user);
    }
    public void deleteUser(User user)
    {
        repo.deleteUser(user);
    }
    public void deleteAllUsers()
    {
        repo.deleteAllUsers();
    }
    public LiveData<List<User>> getAllUsers()
    {
        return users;
    }


}
