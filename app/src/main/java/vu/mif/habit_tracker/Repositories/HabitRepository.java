package vu.mif.habit_tracker.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import vu.mif.habit_tracker.DAOs.habitDAO;
import vu.mif.habit_tracker.Models.Habit;
import vu.mif.habit_tracker.roomDB;

//This repository will be linked to the activities that require actions with Habits and will provide the methods while doing all the work
public class HabitRepository {
    private habitDAO habitDAO;
    private LiveData<List<Habit>> habits;

    public HabitRepository(Application application)
    {
        roomDB database = roomDB.getInstance(application);
        habitDAO = database.getHabitDAO();
        habits = habitDAO.getAllHabits();
    }

    // These methods are the only thing that view model will see and they have such shit structure because they need to be run on a background task
    public void insertHabit(Habit Habit) { new HabitRepository.InsertHabitAsyncTask(habitDAO).execute(Habit); }
    public void updateHabit(Habit Habit) { new HabitRepository.UpdateHabitAsyncTask(habitDAO).execute(Habit); }
    public void deleteHabit(Habit Habit) { new HabitRepository.DeleteHabitAsyncTask(habitDAO).execute(Habit); }
    public void deleteAllHabits() { new HabitRepository.DeleteAllHabitsTask(habitDAO).execute(); }
    public LiveData<List<Habit>> getAllHabits() { return habits; }


    private static class InsertHabitAsyncTask extends AsyncTask<Habit, Void, Void>
    {
        private vu.mif.habit_tracker.DAOs.habitDAO habitDAO;
        private InsertHabitAsyncTask(habitDAO habitDAO)
        {
            this.habitDAO = habitDAO;
        }

        @Override
        protected Void doInBackground(Habit... Habit) {
            habitDAO.insert(Habit[0]);
            return null;
        }
    }
    private static class DeleteHabitAsyncTask extends AsyncTask<Habit, Void, Void>
    {
        private habitDAO habitDAO;
        private DeleteHabitAsyncTask(habitDAO habitDAO)
        {
            this.habitDAO = habitDAO;
        }

        @Override
        protected Void doInBackground(Habit... Habit) {
            habitDAO.delete(Habit[0]);
            return null;
        }
    }
    private static class UpdateHabitAsyncTask extends AsyncTask<Habit, Void, Void>
    {
        private habitDAO habitDAO;
        private UpdateHabitAsyncTask(habitDAO habitDAO)
        {
            this.habitDAO = habitDAO;
        }

        @Override
        protected Void doInBackground(Habit... Habit) {
            habitDAO.update(Habit[0]);
            return null;
        }
    }
    private static class DeleteAllHabitsTask extends AsyncTask<Void, Void, Void>
    {
        private habitDAO habitDAO;
        private DeleteAllHabitsTask(habitDAO habitDAO)
        {
            this.habitDAO = habitDAO;
        }

        @Override
        protected Void doInBackground(Void... Habit) {
            habitDAO.deleteAllHabits();
            return null;
        }
    }

}
