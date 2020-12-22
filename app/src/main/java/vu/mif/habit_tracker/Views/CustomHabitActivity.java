package vu.mif.habit_tracker.Views;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import petrov.kristiyan.colorpicker.ColorPicker;
import vu.mif.habit_tracker.Fragments.DatePickerFragment;
import vu.mif.habit_tracker.Fragments.iconPicker;
import vu.mif.habit_tracker.Models.Habit;
import vu.mif.habit_tracker.R;
import vu.mif.habit_tracker.ViewModels.CustomHabitViewModel;

public class CustomHabitActivity extends AppCompatActivity implements View.OnClickListener, iconPicker.iconPickerListener, DatePickerDialog.OnDateSetListener {

    // Color picker
    int[] colors;
    private int[] pictureID = {R.drawable.book, R.drawable.juice, R.drawable.alarm_clock, R.drawable.bicycle, R.drawable.cleaning, R.drawable.water, R.drawable.wallet, R.drawable.workplace};

    AppCompatButton submitCustomHabitBtn;
    CustomHabitViewModel viewModel;

    EditText customHabitName;
    ImageButton colorPickerBtn;
    ImageButton iconPickerBtn;
    iconPicker pickerDialog;
    private EditText etDays;
    private EditText etTotal;
    private TextView tvDate;

    CustomHabitActivity context;

    private LinearLayout containerRepeatableOn;
    private LinearLayout containerRepeatableOff;
    private LinearLayout containerDate;

    // TODO: Update fields when UI will be updated
    private String name;
    private String iconID;
    private int colourID;
    private boolean isRepeatable = false;
    private int repeatNumber = 1;
    private int endYear = 0;
    private int endMonth = 0;
    private int endDayOfMonth = 0;
    private int totalProgress = 1;
    private int currentProgress = 0;

    private boolean useDate = false;


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

        containerRepeatableOn = findViewById(R.id.containerRepeatableOn);
        containerRepeatableOff = findViewById(R.id.containerRepeatableOff);
        containerDate = findViewById(R.id.containerDate);

        etDays = findViewById(R.id.etDays);
        etTotal = findViewById(R.id.etTotal);

        tvDate = findViewById(R.id.tvDate);
        tvDate.setOnClickListener(this);

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

            if (name.equals("")) {
                sendShortText("Please fill in name field.");
                return;
            }

            String totalAmount = etTotal.getText().toString();

            if (totalAmount.equals("") || Integer.parseInt(totalAmount) <= 0){
                sendShortText("Goal must be higher than 0!");
                return;
            }

            String numberOfDays = etDays.getText().toString();

            if (isRepeatable) {
                if (!numberOfDays.equals("")){
                    if (Integer.parseInt(numberOfDays) <= 0){
                        sendShortText("Days must be higher than 0!");
                        return;
                    }
                    repeatNumber = Integer.parseInt(numberOfDays);

                    Calendar dateNow = Calendar.getInstance();
                    dateNow.add(Calendar.DATE, repeatNumber);

                    endYear = dateNow.get(Calendar.YEAR);
                    endMonth = dateNow.get(Calendar.MONTH);
                    endDayOfMonth = dateNow.get(Calendar.DAY_OF_MONTH);
                } else {
                    Calendar dateNow = Calendar.getInstance();
                    dateNow.add(Calendar.DATE, repeatNumber);

                    endYear = dateNow.get(Calendar.YEAR);
                    endMonth = dateNow.get(Calendar.MONTH);
                    endDayOfMonth = dateNow.get(Calendar.DAY_OF_MONTH);
                }
            } else {
                Calendar habitDate = Calendar.getInstance();
                habitDate.set(Calendar.YEAR, endYear);
                habitDate.set(Calendar.MONTH, endMonth);
                habitDate.set(Calendar.DAY_OF_MONTH, endDayOfMonth);

                Calendar dateNow = Calendar.getInstance();

                if (habitDate.get(Calendar.YEAR) == dateNow.get(Calendar.YEAR) &&
                        habitDate.get(Calendar.DAY_OF_YEAR) == dateNow.get(Calendar.DAY_OF_YEAR)){
                    sendShortText("Date must be after this day!");
                    return;
                } else if (dateNow.after(habitDate)){
                        sendShortText("Date must be after this day!");
                        return;
                }
            }

            totalProgress = Integer.parseInt(totalAmount);

            Habit habit = new Habit(name, iconID, colourID, isRepeatable, repeatNumber, endYear,
                    endMonth, endDayOfMonth, totalProgress, currentProgress);

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
        } else if (view == iconPickerBtn)
        {
            pickerDialog = new iconPicker();
            pickerDialog.items = pictureID;
            pickerDialog.show(getSupportFragmentManager(), "icon picker");
        } else if (view == tvDate) {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString = DateFormat.getDateInstance().format(calendar.getTime());

        endYear = year;
        endMonth = month;
        endDayOfMonth = dayOfMonth;

        Calendar dateNow = Calendar.getInstance();

        int index = -2;

        if (calendar.get(Calendar.YEAR) == dateNow.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == dateNow.get(Calendar.DAY_OF_YEAR)){
            index = 0;
        } else if (dateNow.before(calendar)) {
            index = 1;
        } else {
            index = -1;
        }

        sendShortText(String.valueOf(index));

        tvDate.setText(currentDateString);
    }

    private void sendShortText(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // TODO: delete toast
    public void onRepeatableRBClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.repeatableOn:
                if (checked) {
                    isRepeatable = true;
                    containerRepeatableOff.setVisibility(View.GONE);
                    containerRepeatableOn.setVisibility(View.VISIBLE);
                    //Toast.makeText(context, "You clicked on", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.repeatableOff:
                if (checked) {
                    isRepeatable = false;
                    containerRepeatableOn.setVisibility(View.GONE);
                    containerRepeatableOff.setVisibility(View.VISIBLE);
                    //Toast.makeText(context, "You clicked off", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void onRepeatRBClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.dailyBtn:
                if (checked)
                    repeatNumber = 1;
                    break;
            case R.id.weeklyBtn:
                if (checked)
                    repeatNumber = 7;
                    break;
            case R.id.monthlyBtn:
                if(checked)
                    repeatNumber = 30;
                    break;
        }

    }

    public void onSetDateRBClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.endDateOn:
                if (checked){
                    containerDate.setVisibility(View.VISIBLE);
                    useDate = true;
                }
                break;
            case R.id.endDateOff:
                if (checked){
                    containerDate.setVisibility(View.GONE);
                    useDate = false;
                }
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

    @Override
    public void applyIcon(int position) {
        iconID =  getResources().getStringArray(R.array.habit_icons)[position];
        System.out.println(iconID);
        iconPickerBtn.setImageResource(pictureID[position]);
        pickerDialog.dismiss();
    }
}