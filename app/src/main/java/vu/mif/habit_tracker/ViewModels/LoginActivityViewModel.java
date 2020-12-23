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
import vu.mif.habit_tracker.Repositories.FireBaseRepository;
import vu.mif.habit_tracker.Repositories.UserRepository;
import vu.mif.habit_tracker.Views.LoginActivity;
import vu.mif.habit_tracker.Views.MainActivity;



public class LoginActivityViewModel extends AndroidViewModel   {
    private UserRepository userRepository;
    private FireBaseRepository fireBaseRepository;
    private boolean hasCalledCheck = false;
    private Activity context;

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        fireBaseRepository = new FireBaseRepository(application);
    }

    public void logInUser(String email, String password, Activity context)
    {
        this.context = context;
        if(!email.isEmpty() && !password.isEmpty())
        {
            userRepository.loginUser(email, password).addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
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

        userRepository.getUser().observe((LoginActivity)context, this::LoginResult);

    }

    private void LoginResult(User user)
    {
        if(!hasCalledCheck)
        {
            hasCalledCheck = true;
            if(user == null)
            {
                //jei nera, downloadinti is web
               fireBaseRepository.downloadAllData(context);
            }else
            {
                //tikrinam ar naujas useris ar tas kuris yra lokaliai
                if(user.getUID().equals(userRepository.getUID()))
                {
                    //Prisijunge senas useris
                    context.startActivity(new Intent(context, MainActivity.class));
                    context.finish();
                }else
                {
                    //Prisijunge naujas
                    userRepository.purge();
                    fireBaseRepository.downloadAllData(context);
                }
            }

        }

    }


}
