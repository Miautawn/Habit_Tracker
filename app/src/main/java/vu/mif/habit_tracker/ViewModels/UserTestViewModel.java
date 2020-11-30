package vu.mif.habit_tracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.Repositories.UserRepository;

//This is our viewmodel, which is designed for the registerActivity
public class UserTestViewModel extends AndroidViewModel {
    private UserRepository repo;
    private LiveData<User> user;

    public UserTestViewModel(@NonNull Application application) {
        super(application);
        repo = new UserRepository(application);
        user = repo.getUser();
    }

    public void insertUser(User user)
    {
        if(this.user.getValue() == null) repo.insertUser(user);
    }
    public void updateUser(User user)
    {
        repo.updateUser(user);
    }
    public void deleteUser()
    {
        repo.deleteUser();
    }
    public LiveData<User> getUser()
    {
        return user;
    }

}
