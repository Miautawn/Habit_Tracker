package vu.mif.habit_tracker.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import vu.mif.habit_tracker.R;

public class NewHabitActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton createCustomHabitBtn;

    private ImageButton defaultAlarmBtn;
    private ImageButton defaultBicycleBtn;
    private ImageButton defaultBookBtn;
    private ImageButton defaultCleaningBtn;
    private ImageButton defaultWaterBtn;
    private ImageButton defaultWalletBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_habit);

        Toolbar toolbar = findViewById(R.id.nhToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //setting up default buttons
        defaultAlarmBtn = findViewById(R.id.alarmBtn);
        defaultAlarmBtn.setOnClickListener(this);
        defaultBicycleBtn = findViewById(R.id.bicycleBtn);
        defaultBicycleBtn.setOnClickListener(this);
        defaultBookBtn = findViewById(R.id.bookBtn);
        defaultBookBtn.setOnClickListener(this);
        defaultCleaningBtn = findViewById(R.id.cleaningBtn);
        defaultCleaningBtn.setOnClickListener(this);
        defaultWaterBtn = findViewById(R.id.waterBtn);
        defaultWaterBtn.setOnClickListener(this);
        defaultWalletBtn = findViewById(R.id.walletBtn);
        defaultWalletBtn.setOnClickListener(this);
        //setting up default buttons

        createCustomHabitBtn = findViewById(R.id.createCustomHabitBtn);
        createCustomHabitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == createCustomHabitBtn) {
            Intent intent = new Intent(NewHabitActivity.this, CustomHabitActivity.class);
            startActivity(intent);
        }

        else if (view == defaultAlarmBtn)
        {
            Intent intent = new Intent(NewHabitActivity.this, CustomHabitActivity.class);
            intent.putExtra("DEFAULT_ICON", getResources().getStringArray(R.array.habit_icons)[2]);
            intent.putExtra("DEFAULT_ICON_ID", R.drawable.alarm_clock);
            intent.putExtra("DEFAULT_NAME", "Wake up early");
            startActivity(intent);
        }
        else if (view == defaultBicycleBtn)
        {
            Intent intent = new Intent(NewHabitActivity.this, CustomHabitActivity.class);
            intent.putExtra("DEFAULT_ICON", getResources().getStringArray(R.array.habit_icons)[3]);
            intent.putExtra("DEFAULT_ICON_ID", R.drawable.bicycle);
            intent.putExtra("DEFAULT_NAME", "Workout");
            startActivity(intent);
        }
        else if (view == defaultBookBtn)
        {
            Intent intent = new Intent(NewHabitActivity.this, CustomHabitActivity.class);
            intent.putExtra("DEFAULT_ICON", getResources().getStringArray(R.array.habit_icons)[0]);
            intent.putExtra("DEFAULT_ICON_ID", R.drawable.book);
            intent.putExtra("DEFAULT_NAME", "Study");
            startActivity(intent);
        }
        else if (view == defaultCleaningBtn)
        {
            Intent intent = new Intent(NewHabitActivity.this, CustomHabitActivity.class);
            intent.putExtra("DEFAULT_ICON", getResources().getStringArray(R.array.habit_icons)[4]);
            intent.putExtra("DEFAULT_ICON_ID", R.drawable.cleaning);
            intent.putExtra("DEFAULT_NAME", "Clean up");
            startActivity(intent);
        }
        else if (view == defaultWaterBtn)
        {
            Intent intent = new Intent(NewHabitActivity.this, CustomHabitActivity.class);
            intent.putExtra("DEFAULT_ICON", getResources().getStringArray(R.array.habit_icons)[5]);
            intent.putExtra("DEFAULT_ICON_ID", R.drawable.water);
            intent.putExtra("DEFAULT_NAME", "Hydrate");
            startActivity(intent);
        }
        else if (view == defaultWalletBtn)
        {
            Intent intent = new Intent(NewHabitActivity.this, CustomHabitActivity.class);
            intent.putExtra("DEFAULT_ICON", getResources().getStringArray(R.array.habit_icons)[6]);
            intent.putExtra("DEFAULT_ICON_ID", R.drawable.wallet);
            intent.putExtra("DEFAULT_NAME", "Donate to a Nigerian Prince");
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