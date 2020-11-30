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
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.List;

import vu.mif.habit_tracker.Models.Habit;
import vu.mif.habit_tracker.R;
import vu.mif.habit_tracker.ViewModels.CustomHabitViewModel;

public class CustomHabitActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatButton submitCustomHabitBtn;
    CustomHabitViewModel viewModel;

    EditText customHabitName;

    // TODO: Update fields when UI will be updated
    private String name;
    private int iconID = R.drawable.wallet;
    private int colourID = R.color.pink;
    private boolean isRepeatble = true;
    private int repeatNumber = 0;
    private String endDate = "";
    private int endGoal = 0;
    private boolean isDaily = false;
    private int dailyGoal = 0;
    private boolean hasNotifications = true;

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
        customHabitName = findViewById(R.id.customHabitName);

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
            name = customHabitName.getText().toString();

            Habit habit = new Habit(name, iconID, colourID, isRepeatble, repeatNumber, endDate,
                    endGoal, isDaily, dailyGoal, hasNotifications);

            viewModel.insertHabit(habit);

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    public void onRepeatRBClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.dailyBtn:
                if (checked)
                    isDaily = true;
                    isRepeatble = true;
                    repeatNumber = -1;
                    break;
            case R.id.weeklyBtn:
                if (checked)
                    isRepeatble = true;
                    repeatNumber = -2;
                    break;
            case R.id.monthlyBtn:
                if(checked)
                    isRepeatble = true;
                    repeatNumber = -3;
                    break;
        }

    }

    public void onSetDateRBClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.endDateOn:
                if (checked)

                break;
            case R.id.endDateOff:
                if (checked)

                break;
        }
    }

    public void onSetGoalRBClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.endGoalOn:
                if (checked)

                    break;
            case R.id.endGoalOff:
                if (checked)

                    break;
        }
    }

    public void onSetReminderRBClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.endReminderOn:
                if (checked)
                    hasNotifications = true;
                break;
            case R.id.endReminderOff:
                if (checked)
                    hasNotifications = false;
                break;
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