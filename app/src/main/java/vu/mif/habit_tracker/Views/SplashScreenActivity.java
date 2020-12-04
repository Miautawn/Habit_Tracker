package vu.mif.habit_tracker.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Fix issues with navigation buttons

        startActivity(new Intent(SplashScreenActivity.this,
                LoginActivity.class));
        finish();
    }
}