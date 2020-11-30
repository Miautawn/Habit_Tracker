package vu.mif.habit_tracker.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import vu.mif.habit_tracker.Models.Habit;
import vu.mif.habit_tracker.R;
import vu.mif.habit_tracker.ViewModels.CustomHabitViewModel;

public class CustomHabitActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatButton submitCustomHabitBtn;
    CustomHabitViewModel viewModel;

    //Cia tas naudojamas listas
    List<Habit> habits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setNavigationBarColor(getResources().getColor(R.color.darkGrey, null));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_habit);

        Toolbar toolbar = findViewById(R.id.nhToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        submitCustomHabitBtn = findViewById(R.id.submitCustomHabitBtn);
        submitCustomHabitBtn.setOnClickListener(this);

        //initializing viewmodel and obtaining the list of habits
        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(CustomHabitViewModel.class);
        viewModel.getAllHabits().observe(this, new Observer<List<Habit>>() {
            @Override
            public void onChanged(List<Habit> habits) {
                CustomHabitActivity.this.habits = habits;
            }
        });


    }

    @Override
    public void onClick(View view) {
        if (view == submitCustomHabitBtn) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
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