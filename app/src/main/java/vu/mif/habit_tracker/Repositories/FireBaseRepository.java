package vu.mif.habit_tracker.Repositories;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import vu.mif.habit_tracker.Models.Habit;
import vu.mif.habit_tracker.Models.Pet;
import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.Views.LoginActivity;
import vu.mif.habit_tracker.Views.MainActivity;
import vu.mif.habit_tracker.Views.RegisterActivity;
import vu.mif.habit_tracker.firebaseDB;
import vu.mif.habit_tracker.roomDB;

public class FireBaseRepository {

    private FirebaseAuth auth;
    private roomDB database;
    private FirebaseDatabase fireDB;
    private FirebaseStorage fireStorage;
    private UserRepository userRepository;
    private PetRepository petRepository;
    private HabitRepository habitRepository;

    public FireBaseRepository(Application context)
    {
        this.database = roomDB.getInstance(context);
        this.fireDB = FirebaseDatabase.getInstance();
        this.auth = firebaseDB.getAuthInstance();
        this.fireDB = firebaseDB.getDatabaseInstance();
        this.fireStorage = firebaseDB.getStorageInstance();
        this.userRepository = new UserRepository(context);
        this.petRepository = new PetRepository(context);
        this.habitRepository = new HabitRepository(context);
    }

    public void downloadAllData(Activity login_context)
    {
        firebaseDB.Friends.clear();
        firebaseDB.FriendImages.clear();
        firebaseDB.areFriendsDownloaded = false;
       DownloadUserProfilePicture(login_context);
    }


    private void DownloadHabits(Activity login_context)
    {
        DatabaseReference myRef = fireDB.getReference("/Habits/"+userRepository.getUID());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null)
                {
                    for (DataSnapshot habit: snapshot.getChildren())
                    {
                        Habit downloadedHabit = habit.getValue(Habit.class);
                        habitRepository.insertHabit(downloadedHabit);
                    }
                    login_context.startActivity(new Intent(login_context, MainActivity.class));
                    login_context.finish();
                } else
                {
                    //just continue without habits
                    login_context.startActivity(new Intent(login_context, MainActivity.class));
                    login_context.finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getMessage());
                failedDownload(login_context);
            }
        });
    }

    private void DownloadPet(Activity login_context)
    {
        DatabaseReference myRef = fireDB.getReference("/Pets/" + userRepository.getUID());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null)
                {
                    Pet downloadedPet = snapshot.getValue(Pet.class);
                    petRepository.insertPet(new Pet("Alfonsas", downloadedPet.getAksesuaras()));
                    DownloadHabits(login_context);
                } else
                {
                    //if no pet was found, insert a new one and continue downloading the habits
                    petRepository.insertPet(new Pet("Alfonsas2", 0));
                    DownloadHabits(login_context);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getMessage());
                failedDownload(login_context);
            }
        });
    }

    private void DownloadUser(Activity login_context, String imagePath)
    {
        DatabaseReference myRef = fireDB.getReference("/Users/" + userRepository.getUID());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null)
                {
                    User downloadedUser = snapshot.getValue(User.class);
                    userRepository.insertUser(new User(downloadedUser.getUsername(), downloadedUser.getCurrency(), downloadedUser.getPoints(), imagePath, userRepository.getUID()));
                    DownloadPet(login_context);
                }else failedDownload(login_context);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getMessage());
                failedDownload(login_context);
            }
        });
    }

    private void DownloadUserProfilePicture(Activity login_context)
    {
        StorageReference storageRef = fireStorage.getReference();
        StorageReference myRef = storageRef.child("UserImages/" + userRepository.getUID());
        myRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                    File userImage = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "TackleProfile.png");
                    if(userImage.exists()) userImage.delete();
                    try
                    {
                        FileOutputStream out = new FileOutputStream(userImage.getAbsolutePath());
                        out.write(bytes);
                        out.close();

                        //continuing to download user
                        DownloadUser(login_context, userImage.getAbsolutePath());
                    }catch (Exception e)
                    {
                        //continue download without user profile
                        System.out.println();
                        DownloadUser(login_context, null);
                    }
                }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //continue download without user profile
                System.out.println(e.getMessage());
                DownloadUser(login_context, null);
            }
        });
    }

    private void failedDownload(Activity login_context)
    {
        new AlertDialog.Builder(login_context).setTitle("Download Failed").setMessage("Something went wrong when downloading your account data, we recommend you to create new account").setPositiveButton("Oh no...", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent( login_context, RegisterActivity.class);
                login_context.startActivity(intent);
            }
        }).create().show();
    }



}
