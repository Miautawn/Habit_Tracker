package vu.mif.habit_tracker.Repositories;

import android.app.Application;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import vu.mif.habit_tracker.DAOs.userDAO;
import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.firebaseDB;
import vu.mif.habit_tracker.roomDB;


//This repository will be linked to the RegisterActivity and will provide the methods while doing all the work
public class UserRepository {
    private userDAO userDAO;
    private LiveData<User> user;
    private FirebaseAuth auth;
    private roomDB database;
    private FirebaseDatabase fireDB;
    private FirebaseStorage fireStorage;

    public UserRepository(Application application)
    {
        database = roomDB.getInstance(application);
        this.userDAO = database.getUserDAO();
        this.user = userDAO.getUser();
        this.auth = firebaseDB.getAuthInstance();
        this.fireStorage = firebaseDB.getStorageInstance();
        this.fireDB = firebaseDB.getDatabaseInstance();
    }

    // These methods are the only thing that view model will see and they have such shit structure because they need to be run on a background task
    public void insertUser(User user) {new InsertUserAsyncTask(userDAO).execute(user); }
    public void updateUser(User user)
    {
        new UpdateUserAsyncTask(userDAO).execute(user);
    }
    public void deleteUser() { new DeleteUserTask(userDAO).execute(); }
    public LiveData<User> getUser() { return user; }
    public boolean isLogedIn() { return auth.getCurrentUser() != null; };
    public void disconnectUser() { if(isLogedIn()) auth.signOut(); }
    public String getUID() {if(isLogedIn()) return auth.getCurrentUser().getUid(); return null;}

    //Firebase methods
    public Task<AuthResult> loginUser(String email, String password) { return auth.signInWithEmailAndPassword(email, password); }
    public Task<AuthResult> registerUser(String email, String password) { return auth.createUserWithEmailAndPassword(email, password); }
    public DatabaseReference uploadUser() {
        DatabaseReference myRef = fireDB.getReference("/Users");
        return myRef.child(auth.getCurrentUser().getUid());
    }
    public UploadTask UploadProfilePicture(Uri image) {
        StorageReference storageRef = fireStorage.getReference();
        StorageReference myRef = storageRef.child("UserImages/"+ auth.getCurrentUser().getUid());
        return myRef.putFile(image);
    }
    public Query DownloadPotentialFriends(String typed_username) {
        DatabaseReference myRef = fireDB.getReference("/Users/");
        return myRef.orderByChild("username").startAt(typed_username).endAt(typed_username+"\uf8ff").limitToFirst(5);
    }
    public DatabaseReference UploadNewFriend(String new_UID)
    {
        DatabaseReference myRef = fireDB.getReference("/Friends/"+getUID());
        return myRef.child(new_UID);
    }


    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void>
    {
        private userDAO userDAO;
        private InsertUserAsyncTask(userDAO userDAO)
        {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(User... user) {
            userDAO.insert(user[0]);
            return null;
        }
    }
    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void>
    {
        private userDAO userDAO;
        private UpdateUserAsyncTask(userDAO userDAO)
        {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(User... user) {
            userDAO.update(user[0]);
            return null;
        }
    }
    private static class DeleteUserTask extends AsyncTask<Void, Void, Void>
    {
        private userDAO userDAO;
        private DeleteUserTask(userDAO userDAO)
        {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(Void... user) {
            userDAO.deleteUser();
            return null;
        }
    }
    private static class PurgeTask extends AsyncTask<Void, Void, Void>
    {
        private roomDB database;
        private PurgeTask(roomDB database)
        {
            this.database = database;
        }

        @Override
        protected Void doInBackground(Void... user) {
            database.clearAllTables();
            return null;
        }
    }

    //big No no
    public void purge()
    {
        new PurgeTask(database).execute();
    }

}
