package vu.mif.habit_tracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import vu.mif.habit_tracker.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatButton emailBtn;
    TextView loginTextRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailBtn = findViewById(R.id.emailBtn);
        emailBtn.setOnClickListener(this);

        loginTextRedirect = findViewById(R.id.logInText);
        loginTextRedirect.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == emailBtn) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else if (view == loginTextRedirect) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}