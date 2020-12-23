package vu.mif.habit_tracker.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

import vu.mif.habit_tracker.Models.Habit;
import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.ViewModels.MainActivityViewModel;
import vu.mif.habit_tracker.ViewModels.SplashScreenViewModel;
import vu.mif.habit_tracker.firebaseDB;

public class SplashScreenActivity extends AppCompatActivity {

    private SplashScreenViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SplashScreenViewModel.class);

        // TODO: Fix issues with navigation buttons

        //getting the user from database
        viewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user != null)
                {
                    if(viewModel.isLogedIn())
                    {
                        //if user exists, and is logged in
                        startActivity(new Intent(SplashScreenActivity.this,
                                MainActivity.class));
                        finish();

                    }else
                    {
                        //if user exists, but is not logged in
                        LogIn();
                    }

                }
                else
                {
                    //if user does not exist, proceed to login screen
                    LogIn();
                }
            }
        });

        viewModel.getAllHabits().observe(this, new Observer<List<Habit>>() {
            @Override
            public void onChanged(List<Habit> habits) {
                updateHabitDatabase(habits);
            }
        });
    }
    private void LogIn()
    {
            startActivity(new Intent(SplashScreenActivity.this,
                    LoginActivity.class));
            finish();
    }

    private void updateHabitDatabase(List<Habit> habits) {
        for (Habit habit: habits) {
            if (habit.getEndYear() != 0) {
                if (habit.isRepeatble()) {
                    if (checkIfDateNowAndAfter(habit.getEndYear(), habit.getEndMonth(), habit.getEndDayOfMonth())){
                        int repeatNumber = habit.getRepeatNumber();

                        Calendar habitDate = Calendar.getInstance();
                        habitDate.set(Calendar.YEAR, habit.getEndYear());
                        habitDate.set(Calendar.MONTH, habit.getEndMonth());
                        habitDate.set(Calendar.DAY_OF_MONTH, habit.getEndDayOfMonth());

                        habitDate.add(Calendar.DATE, repeatNumber);

                        habit.setEndYear(habitDate.get(Calendar.YEAR));
                        habit.setEndMonth(habitDate.get(Calendar.MONTH));
                        habit.setEndDayOfMonth(habitDate.get(Calendar.DAY_OF_MONTH));

                        habit.setCurrentProgress(0);

                        viewModel.updateHabit(habit);
                    }
                } else {
                    if (checkIfDateNowAndAfter(habit.getEndYear(), habit.getEndMonth(), habit.getEndDayOfMonth())){
                        viewModel.deleteHabit(habit);
                    }
                }
            }
        }
    }

    private boolean checkIfDateNowAndAfter(int endYear, int endMonth, int endDayOfMonth) {
        Calendar habitDate = Calendar.getInstance();
        habitDate.set(Calendar.YEAR, endYear);
        habitDate.set(Calendar.MONTH, endMonth);
        habitDate.set(Calendar.DAY_OF_MONTH, endDayOfMonth);

        Calendar dateNow = Calendar.getInstance();

        if (habitDate.get(Calendar.YEAR) == dateNow.get(Calendar.YEAR) &&
                habitDate.get(Calendar.DAY_OF_YEAR) == dateNow.get(Calendar.DAY_OF_YEAR)){
            return true;
        } else if (dateNow.after(habitDate)){
            return true;
        }
        return false;
    }
}