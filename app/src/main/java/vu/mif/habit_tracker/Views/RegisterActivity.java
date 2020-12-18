package vu.mif.habit_tracker.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import vu.mif.habit_tracker.Models.Habit;
import vu.mif.habit_tracker.Models.Pet;
import vu.mif.habit_tracker.R;
import vu.mif.habit_tracker.ViewModels.HabitTestViewModel;
import vu.mif.habit_tracker.ViewModels.LoginActivityViewModel;
import vu.mif.habit_tracker.ViewModels.PetTestViewModel;
import vu.mif.habit_tracker.ViewModels.RegisterActivityViewModel;

public class RegisterActivity extends AppCompatActivity {

    private ImageButton passwordVisibilityToggle;
    private EditText passwordField;
    private boolean isPasswordVisible = false;
    private Button btnSignIn;
    private EditText email;
    private EditText username;
    private RegisterActivityViewModel viewmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setNavigationBarColor(getResources().getColor(R.color.transparentBlack, null));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        viewmodel = new ViewModelProvider(this).get(RegisterActivityViewModel.class);

        Toolbar toolbar = findViewById(R.id.registerToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        passwordVisibilityToggle = findViewById(R.id.password_visibility_toggle);
        passwordField = findViewById(R.id.passwordField);

        email= findViewById(R.id.signinEmailEditText);
        username = findViewById(R.id.signiUserNameEditText);
        btnSignIn = findViewById(R.id.signinBtn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewmodel.registerUser(username.getText().toString(), email.getText().toString(), passwordField.getText().toString(), RegisterActivity.this);
            }
        });

        passwordVisibilityToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordVisible) {
                    passwordField.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordField.setSelection(passwordField.length());
                    isPasswordVisible = false;

                } else {
                    passwordField.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordField.setSelection(passwordField.length());
                    isPasswordVisible = true;
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}