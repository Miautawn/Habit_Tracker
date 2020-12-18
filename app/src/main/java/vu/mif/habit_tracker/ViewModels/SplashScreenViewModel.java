package vu.mif.habit_tracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.Repositories.UserRepository;

public class SplashScreenViewModel extends AndroidViewModel {
    private UserRepository repo;
    private LiveData<User> user;

    public SplashScreenViewModel(@NonNull Application application) {
        super(application);
        repo = new UserRepository(application);
        user = repo.getUser();
    }

    public LiveData<User> getUser()
    {
        return user;
    }
}
