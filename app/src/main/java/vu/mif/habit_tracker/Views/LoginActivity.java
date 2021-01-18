package vu.mif.habit_tracker.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import vu.mif.habit_tracker.R;
import vu.mif.habit_tracker.ViewModels.LoginActivityViewModel;
import vu.mif.habit_tracker.ViewModels.MainActivityViewModel;
import vu.mif.habit_tracker.Views.MainActivity;
import  vu.mif.habit_tracker.firebaseDB;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private int STORAGE_PERMISION_CODE = 15;

    AppCompatButton emailBtn;
    TextView loginTextRedirect;

    ImageButton passwordVisibilityToggle;
    private boolean isPasswordVisible = false;

    //cia demo stuff
    EditText email;
    EditText password;
    Button btnLogIn;
    Button btnSignIn;
    LoginActivityViewModel viewmodel;
    ProgressBar login_loader;
    TextView login_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_demo);

        viewmodel = new ViewModelProvider(this).get(LoginActivityViewModel.class);
        email = findViewById(R.id.loginEmailEditText);
        password = findViewById(R.id.loginPasswordField);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnLogIn.setOnClickListener(this);
        btnSignIn = findViewById(R.id.signinBtn);
        login_loader = findViewById(R.id.loading_login);
        login_message = findViewById(R.id.login_message);

        passwordVisibilityToggle = findViewById(R.id.password_visibility_toggle);

        passwordVisibilityToggle.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == emailBtn) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }else if(view == btnLogIn)
        {
            if(firebaseDB.CheckPermission(this, STORAGE_PERMISION_CODE))
            {
                //closing the keyboard
                View mView = this.getCurrentFocus();
                if(mView != null)
                {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
                }
                viewmodel.logInUser(email.getText().toString(), password.getText().toString(), this);
            }
        } else if (view == btnSignIn) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        } else if (view == loginTextRedirect) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        } else if (view == passwordVisibilityToggle) {
            if (isPasswordVisible) {
                password.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                password.setSelection(password.length());
                isPasswordVisible = false;

            } else {
                password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                password.setSelection(password.length());
                isPasswordVisible = true;
            }
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

    public void updateLoginLoading(int CODE, String message)
    {
        if(message == null) message = "Nothing is wrong";

        if(CODE == 1)
        {
            login_loader.setVisibility(View.VISIBLE);
            login_message.setVisibility(View.GONE);

            //disable user interaction
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }else {
            login_loader.setVisibility(View.GONE);
            login_message.setText(message);
            login_message.setVisibility(View.VISIBLE);

            //Re-enabling the interaction
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

}