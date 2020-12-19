package vu.mif.habit_tracker.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import vu.mif.habit_tracker.R;
import vu.mif.habit_tracker.ViewModels.LoginActivityViewModel;
import vu.mif.habit_tracker.ViewModels.MainActivityViewModel;
import vu.mif.habit_tracker.Views.MainActivity;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatButton emailBtn;
    TextView loginTextRedirect;

    //cia demo stuff
    EditText email;
    EditText password;
    Button btnLogIn;
    Button btnSignIn;
    LoginActivityViewModel viewmodel;

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
        btnSignIn.setOnClickListener(this);


//TODO: nuspresti del galutinio log in dizaino

//      emailBtn = findViewById(R.id.emailBtn);
//      emailBtn.setOnClickListener(this);


        //  loginTextRedirect = findViewById(R.id.logInText);
        //  loginTextRedirect.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == emailBtn) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }else if(view == btnLogIn)
        {
            viewmodel.logInUser(email.getText().toString(), password.getText().toString(), this);
        } else if (view == btnSignIn) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
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