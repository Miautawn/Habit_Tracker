package vu.mif.habit_tracker.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

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
    }
    private void LogIn()
    {
            startActivity(new Intent(SplashScreenActivity.this,
                    LoginActivity.class));
            finish();
    }


}