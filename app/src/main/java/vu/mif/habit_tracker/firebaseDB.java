package vu.mif.habit_tracker;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import vu.mif.habit_tracker.Views.MainActivity;

    public abstract class firebaseDB {
    private static FirebaseAuth auth;
    private static FirebaseDatabase database;
    private static FirebaseStorage storage;

    public static synchronized FirebaseAuth getAuthInstance()
    {
        if(auth == null) auth = FirebaseAuth.getInstance();
        return auth;
    }
    public static synchronized  FirebaseDatabase getDatabaseInstance()
    {
        if(database == null) database = FirebaseDatabase.getInstance();
        return database;
    }
    public static synchronized FirebaseStorage getStorageInstance()
    {
        if(storage == null) storage = FirebaseStorage.getInstance();
        return storage;
    }

    public static boolean CheckPermission(Activity context, int REQUEST_CODE)
    {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                new AlertDialog.Builder(context).setTitle("Permision Needed").setMessage("Please grant External Read and Write permissions in order to be able to change profile picture").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(context, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                return false;
            }else if(!ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_EXTERNAL_STORAGE) || !ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
                new AlertDialog.Builder(context).setTitle("Permision Needed").setMessage("Please grant External Read and Write permissions in order to be able to change profile picture").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(context, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                return false;
            }
            else
            {
                ActivityCompat.requestPermissions(context, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                return false;
            }

        }

    }




}