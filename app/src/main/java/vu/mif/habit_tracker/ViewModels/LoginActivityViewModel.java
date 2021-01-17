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
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;

import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.Repositories.FireBaseRepository;
import vu.mif.habit_tracker.Repositories.UserRepository;
import vu.mif.habit_tracker.Views.LoginActivity;
import vu.mif.habit_tracker.Views.MainActivity;
import vu.mif.habit_tracker.firebaseDB;



public class LoginActivityViewModel extends AndroidViewModel   {
    private UserRepository userRepository;
    private FireBaseRepository fireBaseRepository;
    private boolean hasCalledCheck = false;
    private Activity context;
    private LoginActivity loginActivity;

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        fireBaseRepository = new FireBaseRepository(application);
    }

    public void logInUser(String email, String password, Activity context)
    {
        this.context = context;
        if(loginActivity == null) loginActivity = (LoginActivity)context;
        if(firebaseDB.CheckOnlineStatus(context)) {
            if (!email.isEmpty() && !password.isEmpty()) {
                loginActivity.updateLoginLoading(1, null);
                userRepository.LoginUser(email, password).addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            SuccessfulLogin();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseTooManyRequestsException e) {
                                loginActivity.updateLoginLoading(2, "Too many requests,\nplease wait and try again later");
                            } catch (FirebaseAuthException e) {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                switch (errorCode) {
                                    case "ERROR_USER_NOT_FOUND":
                                        loginActivity.updateLoginLoading(2, "Such user does not exist!");
                                        break;
                                    case "ERROR_INVALID_EMAIL":
                                        loginActivity.updateLoginLoading(2, "Please enter the email correctly!");
                                        break;
                                    case "ERROR_WRONG_PASSWORD":
                                        loginActivity.updateLoginLoading(2, "The password is incorrect!");
                                        break;
                                }
                            } catch (Exception e) {
                                loginActivity.updateLoginLoading(2, "Unknown error");
                            }

                        }
                    }
                });
            } else loginActivity.updateLoginLoading(2, "You must fill in all of the fields");
        } else loginActivity.updateLoginLoading(2, "You must be online to log in");

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
