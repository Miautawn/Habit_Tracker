package vu.mif.habit_tracker.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.ViewModels.MainActivityViewModel;
import vu.mif.habit_tracker.ViewModels.SplashScreenViewModel;

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
                    //if user exists, proceed to main screen
                    startActivity(new Intent(SplashScreenActivity.this,
                            MainActivity.class));
                    finish();
                }
                else
                {
                    //if user does not exist, proceed to login screen
                    startActivity(new Intent(SplashScreenActivity.this,
                            LoginActivity.class));
                    finish();
                }
            }
        });




    }
}