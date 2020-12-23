package vu.mif.habit_tracker.ViewModels;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Contacts;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import vu.mif.habit_tracker.Models.Pet;
import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.Repositories.PetRepository;
import vu.mif.habit_tracker.Repositories.UserRepository;
import vu.mif.habit_tracker.Views.MainActivity;

public class RegisterActivityViewModel extends AndroidViewModel {
    UserRepository userRepo;
    PetRepository petRepo;
    public RegisterActivityViewModel(@NonNull Application application) {
        super(application);
        userRepo = new UserRepository(application);
        petRepo = new PetRepository(application);
    }

    public void registerUser(String username, String email, String password, Activity context)
    {
        if(!email.isEmpty() && !password.isEmpty())
        {
            if(!userRepo.isLogedIn())
            {
                userRepo.registerUser(email, password).addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            userRepo.purge();
                            InsertAndUploadUser(username, userRepo.getUID(), context);
                        }else
                        {
                            System.out.println(task.getException());
                            Toast.makeText(context, task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }else
            {
               InsertAndUploadUser(username, userRepo.getUID(), context);
            }

        }

    }

    private void InsertAndUploadUser(String username, String UID, Activity context)
    {
        User newUser = new User(username, 0, 0, null, UID);
        Pet newPet = new Pet("Alfonsas", 0);
        userRepo.insertUser(newUser);
        petRepo.insertPet(newPet);

        DatabaseReference ref = userRepo.uploadUser();
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
                    new AlertDialog.Builder(context).setTitle("Something went wrong").setMessage("There has been an error when uploading your data in Firestore, but you can  still procced to use this app freely").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            context.startActivity(new Intent(context, MainActivity.class));
                            context.finish();
                        }
                    }).create().show();
                }
            }
        });
    }
}
