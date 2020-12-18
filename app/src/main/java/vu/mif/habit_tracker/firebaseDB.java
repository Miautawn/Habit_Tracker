package vu.mif.habit_tracker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

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



}