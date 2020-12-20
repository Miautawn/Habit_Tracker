package vu.mif.habit_tracker.ViewModels;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.provider.Contacts;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.Repositories.UserRepository;
import vu.mif.habit_tracker.Views.MainActivity;

public class RegisterActivityViewModel extends AndroidViewModel {
    UserRepository repo;
    public RegisterActivityViewModel(@NonNull Application application) {
        super(application);
        repo = new UserRepository(application);
    }

    public void registerUser(String username, String email, String password, Activity context)
    {
        if(!email.isEmpty() && !password.isEmpty())
        {
            if(!repo.isLogedIn())
            {
                repo.registerUser(email, password).addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            repo.purge();
                            InsertAndUpload(username, repo.getUID(), context);
                        }else
                        {
                            System.out.println(task.getException());
                            Toast.makeText(context, task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else
            {
               InsertAndUpload(username, repo.getUID(), context);
            }

        }

    }

    private void InsertAndUpload(String username, String UID, Activity context)
    {
        User newUser = new User(username, 0, null, UID);
        repo.insertUser(newUser);
        DatabaseReference ref = repo.uploadUser();
        ref.setValue(newUser, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error == null)
                {
                    //There was no error and we can proceed
                    context.startActivity(new Intent(context, MainActivity.class));
                    context.finish();
                }else
                {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
