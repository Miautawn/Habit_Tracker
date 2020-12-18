package vu.mif.habit_tracker.ViewModels;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.Repositories.UserRepository;
import vu.mif.habit_tracker.Views.MainActivity;



public class LoginActivityViewModel extends AndroidViewModel   {
    private UserRepository repo;
    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        repo = new UserRepository(application);
    }

    public void registerUser(String email, String password, Activity context)
    {
        if(!email.isEmpty() && !password.isEmpty())
        {
            repo.loginUser(email, password).addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        //TODO: implement data download from firebase
                        repo.insertUser(new User("testas", 100, "nera"));
                        context.startActivity(new Intent(context, MainActivity.class));
                        context.finish();
                    }
                    else
                    {
                        System.out.println(task.getException());
                        Toast.makeText(context, task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

}
