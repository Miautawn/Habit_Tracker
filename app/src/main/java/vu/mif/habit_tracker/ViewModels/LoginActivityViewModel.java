package vu.mif.habit_tracker.ViewModels;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.Repositories.UserRepository;
import vu.mif.habit_tracker.Views.LoginActivity;
import vu.mif.habit_tracker.Views.MainActivity;



public class LoginActivityViewModel extends AndroidViewModel   {
    private UserRepository repo;
    private boolean hasCalledCheck = false;
    private Activity context;

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        repo = new UserRepository(application);
    }

    public void logInUser(String email, String password, Activity context)
    {
        this.context = context;
        if(!email.isEmpty() && !password.isEmpty())
        {
            repo.loginUser(email, password).addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        SuccessfulLogin();
                    }
                    else
                    {
                        Toast.makeText(context, task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else Toast.makeText(getApplication(), "Please fill all of the fields", Toast.LENGTH_SHORT).show();

    }

    private void SuccessfulLogin() {

        repo.getUser().observe((LoginActivity)context, this::tesinys);

    }

    private void tesinys(User user)
    {
        if(!hasCalledCheck)
        {
            hasCalledCheck = true;
            if(user == null)
            {
                //TODO: implement data download from firebase
                //jei nera, downloadinti is web
                repo.insertUser(new User("Downloaded User", 110, "nera", repo.getUID()));
                context.startActivity(new Intent(getApplication(), MainActivity.class));
                context.finish();
            }else
            {
                //tikrinam ar naujas useris ar tas kuris yra lokaliai
                if(user.getUID().equals(repo.getUID()))
                {
                    //Prisijunge senas useris
                    context.startActivity(new Intent(context, MainActivity.class));
                    context.finish();
                }else
                {
                    //Prisijunge naujas
                    //TODO: implement data download from firebase
                    repo.purge();
                    repo.insertUser(new User("Foreign User", 112, "nera", repo.getUID()));
                    context.startActivity(new Intent(context, MainActivity.class));
                    context.finish();
                }
            }

        }

    }


}
