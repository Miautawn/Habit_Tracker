package vu.mif.habit_tracker.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import vu.mif.habit_tracker.DAOs.userDAO;
import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.roomDB;

//This repository will be linked to the RegisterActivity and will provide the methods while doing all the work
public class RegisterRepository {
    private userDAO userDAO;
    private LiveData<List<User>> users;

    public RegisterRepository(Application application)
    {
        roomDB database = roomDB.getInstance(application);
        this.userDAO = database.getUserDAO();
        this.users = userDAO.getAllUsers();
    }

    // These methods are the only thing that view model will see and they have such shit struture because they need to be run on a background task
    public void insertUser(User user)
    {
        new InsertUserAsyncTask(userDAO).execute(user);
    }
    public void updateUser(User user)
    {
        new UpdateUserAsyncTask(userDAO).execute(user);
    }
    public void deleteUser(User user)
    {
        new DeleteUserAsyncTask(userDAO).execute(user);
    }
    public void deleteAllUsers()
    {
        new DeleteAllUsersTask(userDAO).execute();
    }
    public LiveData<List<User>> getAllUsers()
    {
        return users;
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
    private static class DeleteUserAsyncTask extends AsyncTask<User, Void, Void>
    {
        private userDAO userDAO;
        private DeleteUserAsyncTask(userDAO userDAO)
        {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(User... user) {
            userDAO.delete(user[0]);
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
    private static class DeleteAllUsersTask extends AsyncTask<Void, Void, Void>
    {
        private userDAO userDAO;
        private DeleteAllUsersTask(userDAO userDAO)
        {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(Void... user) {
            userDAO.deleteAllUsers();
            return null;
        }
    }
}
