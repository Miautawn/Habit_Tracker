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
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import vu.mif.habit_tracker.Models.Pet;
import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.Repositories.PetRepository;
import vu.mif.habit_tracker.Repositories.UserRepository;
import vu.mif.habit_tracker.Views.MainActivity;
import vu.mif.habit_tracker.Views.RegisterActivity;
import vu.mif.habit_tracker.firebaseDB;


public class RegisterActivityViewModel extends AndroidViewModel {
    private UserRepository userRepo;
    private PetRepository petRepo;
    public RegisterActivityViewModel(@NonNull Application application) {
        super(application);
        userRepo = new UserRepository(application);
        petRepo = new PetRepository(application);
    }

    public void registerUser(String username, String email, String password, Activity context)
    {
        RegisterActivity register_activity = (RegisterActivity)context;
        if(firebaseDB.CheckOnlineStatus(context)) {
            if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                register_activity.updateRegisterLoading(1, null);
                if (!userRepo.isLogedIn()) {
                    userRepo.RegisterUser(email, password).addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                userRepo.purge();
                                InsertAndUpload(username, userRepo.getUID(), context);
                            } else {
                                try {
                                    throw task.getException();
                                } catch (FirebaseTooManyRequestsException e) {
                                    register_activity.updateRegisterLoading(2, "Too many requests,\nplease wait and try again later");
                                } catch (FirebaseAuthException e) {
                                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                    switch (errorCode) {
                                        case "ERROR_INVALID_EMAIL":
                                            register_activity.updateRegisterLoading(2, "Please enter the email correctly!");
                                            break;
                                        case "ERROR_WEAK_PASSWORD":
                                            register_activity.updateRegisterLoading(2, "Weak password,\nit should be at least 6 characters");
                                            break;
                                        case "ERROR_EMAIL_ALREADY_IN_USE":
                                            register_activity.updateRegisterLoading(2, "This email is already taken");
                                            break;
                                    }
                                } catch (Exception e) {
                                    register_activity.updateRegisterLoading(2, "Unknown error");
                                }
                            }
                        }
                    });
                } else {
                    InsertAndUpload(username, userRepo.getUID(), context);
                }

            } else register_activity.updateRegisterLoading(2, "You must fill in all of the fields");
        }else register_activity.updateRegisterLoading(2, "You must be online to register");

    }

    private void InsertAndUpload(String username, String UID, Activity context)
    {
        User newUser = new User(username, 0, 0, null, UID);
        Pet newPet = new Pet("Alfonsas", 0);
        userRepo.insertUser(newUser);
        petRepo.insertPet(newPet);

        DatabaseReference ref = userRepo.UploadUser();
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
                    new AlertDialog.Builder(context).setTitle("Something went wrong").setMessage("There has been an error while uploading your data to Firestore, but you can  still proceed to use this app freely").setPositiveButton("ok", new DialogInterface.OnClickListener() {
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