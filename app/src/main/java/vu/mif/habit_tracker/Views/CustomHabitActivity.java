package vu.mif.habit_tracker.Views;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import petrov.kristiyan.colorpicker.ColorPicker;
import vu.mif.habit_tracker.Fragments.iconPicker;
import vu.mif.habit_tracker.Models.Habit;
import vu.mif.habit_tracker.R;
import vu.mif.habit_tracker.ViewModels.CustomHabitViewModel;

public class CustomHabitActivity extends AppCompatActivity implements View.OnClickListener, iconPicker.iconPickerListener {

    // Color picker
    int[] colors;
    private int[] pictureID = {R.drawable.book, R.drawable.juice, R.drawable.alarm_clock, R.drawable.bicycle, R.drawable.cleaning, R.drawable.water, R.drawable.wallet, R.drawable.workplace};

    AppCompatButton submitCustomHabitBtn;
    CustomHabitViewModel viewModel;

    EditText customHabitName;
    ImageButton colorPickerBtn;
    ImageButton iconPickerBtn;
    iconPicker pickerDialog;

    CustomHabitActivity context;

    // TODO: Update fields when UI will be updated
    private String name;
    private String iconID;
    private int colourID;
    private boolean isRepeatable = true;
    private int repeatNumber = 0;
    private String endDate = "";
    private int totalProgress = 1;
    private int currentProgress = 0;


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
        colorPickerBtn = findViewById(R.id.ibChooseColor);
        iconPickerBtn = findViewById(R.id.ibChooseIcon);
        colorPickerBtn.setOnClickListener(this);
        iconPickerBtn.setOnClickListener(this);
        customHabitName = findViewById(R.id.customHabitName);
        context = this;

        //checking whether there are any values that are passed and setting them accordingly
        if (!getIntent().hasExtra("DEFAULT_ICON_ID"))
        {
            //Setting default icon to be random
            Random rand = new Random();
            int iconRandomID = rand.nextInt(getResources().getStringArray(R.array.habit_icons).length);
            iconID = getResources().getStringArray(R.array.habit_icons)[iconRandomID];
            colourID = getResources().getColor(R.color.initialHabitColor, null);
            iconPickerBtn.setImageResource(pictureID[iconRandomID]);
            //Setting default icon to be random
        }
        else
        {
            iconID = getIntent().getStringExtra("DEFAULT_ICON");
            iconPickerBtn.setImageResource(getIntent().getIntExtra("DEFAULT_ICON_ID", R.drawable.workplace));
            name = getIntent().getStringExtra("DEFAULT_NAME");
            customHabitName.setText(name);
            colourID = getResources().getColor(R.color.initialHabitColor, null);
        }

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


            Habit habit = new Habit(name, iconID, colourID, isRepeatable, repeatNumber, endDate,
                    totalProgress, currentProgress);

            viewModel.insertHabit(habit);

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (view == colorPickerBtn) {
            final ColorPicker colorPicker = new ColorPicker(context);
            colors = getResources().getIntArray(R.array.colors);
            colorPicker.getDialogBaseLayout().setBackgroundColor(getResources().getColor(R.color.grey, null));
            colorPicker
                .setColors(colors)
                .setColumns(4)
                .setRoundColorButton(true)
                .disableDefaultButtons(true)
                .setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
                    @Override
                    public void setOnFastChooseColorListener(int position, int color) {
                        colourID = color;
                        colorPickerBtn.setColorFilter(colourID);

                        colorPicker.dismissDialog();
                    }

                    @Override
                    public void onCancel() {

                    }
                }).show();
        }else if (view == iconPickerBtn)
        {
            pickerDialog = new iconPicker();
            pickerDialog.items = pictureID;
            pickerDialog.show(getSupportFragmentManager(), "icon picker");
        }
    }

    public void onRepeatRBClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.dailyBtn:
                if (checked)
                    isRepeatable = true;
                    repeatNumber = -1;
                    break;
            case R.id.weeklyBtn:
                if (checked)
                    isRepeatable = true;
                    repeatNumber = -2;
                    break;
            case R.id.monthlyBtn:
                if(checked)
                    isRepeatable = true;
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

    private void setColor(@ColorInt int color) {
        Toast.makeText(this, "Changed color", Toast.LENGTH_SHORT);
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

    @Override
    public void applyIcon(int position) {
        iconID =  getResources().getStringArray(R.array.habit_icons)[position];
        System.out.println(iconID);
        iconPickerBtn.setImageResource(pictureID[position]);
        pickerDialog.dismiss();
    }
}