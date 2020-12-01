package vu.mif.habit_tracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.TransitionAdapter;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;

import vu.mif.habit_tracker.Models.Habit;
import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.R;
import vu.mif.habit_tracker.ViewModels.MainActivityViewModel;
import vu.mif.habit_tracker.components.CircularProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int currentIndex = 0;

    private ViewGroup hiddenLeaderBoardOverlay;
    private View overlayTrigger;

    private ImageButton plusBtn;
    private TextView currentHabitName;

    MainActivity context;
    MotionLayout motionLayout;
    MainActivityViewModel model;
    TypedArray habitIcons;

    //Cia habitu listas ir useris
    List<Habit> habits;
    User user;

    CircularProgressBar progressBarLeftTwo;
    CircularProgressBar progressBarLeftOne;
    CircularProgressBar progressBarCenter;
    CircularProgressBar progressBarRightOne;
    CircularProgressBar progressBarRightTwo;

    Habit[] cardHabits = new Habit[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        motionLayout = findViewById(R.id.motionLayout);
        context = this;
        model = new ViewModelProvider(context).get(MainActivityViewModel.class);

        plusBtn = findViewById(R.id.plusBtn);
        hiddenLeaderBoardOverlay = findViewById(R.id.leaderboardOverlayContainer);
        overlayTrigger = findViewById(R.id.leaderboardContainer);
        currentHabitName = findViewById(R.id.currentHabitName);

        progressBarLeftTwo = findViewById(R.id.progressBarLeftTwo);
        progressBarLeftOne = findViewById(R.id.progressBarLeftOne);
        progressBarCenter = findViewById(R.id.progressBarCenter);
        progressBarRightOne = findViewById(R.id.progressBarRightOne);
        progressBarRightTwo = findViewById(R.id.progressBarRightTwo);

        model.getStream().observe(context, this::updateCards);
        model.getAllHabits().observe(context, new Observer<List<Habit>>() {
            @Override
            public void onChanged(List<Habit> habits) {
                context.habits = habits;
                updateUI();
            }
        });
        model.getUser().observe(context, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                context.user = user;
            }
        });

        motionLayout.setTransitionListener(new TransitionAdapter() {
            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                switch (currentId) {
                    case R.id.barLeft:
                        swipeRight();
                        motionLayout.setProgress(0f);
                        break;
                    case R.id.barRight:
                        swipeLeft();
                        motionLayout.setProgress(0f);
                        break;
                }
            }
        });

        // TODO: Fix onClick issue
        overlayTrigger.setOnClickListener(this);
        hiddenLeaderBoardOverlay.setOnClickListener(this);
        plusBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == overlayTrigger) {
            if(!isPanelShown()) {
                slideUpDown(hiddenLeaderBoardOverlay);
            }
        } else if(view == hiddenLeaderBoardOverlay) {
            if(isPanelShown()) {
                slideUpDown(hiddenLeaderBoardOverlay);
            }
        } else if(view == plusBtn) {
            Intent intent = new Intent(MainActivity.this, NewHabitActivity.class);
            startActivity(intent);
        }
    }

    private void updateUI() {
        if (!habits.isEmpty()){
            updateData();
            // UI flicker fix :)
            model.updateCards(cardHabits);
        }
    }

    private void updateData() {
        cardHabits[0] = habits.get(currentIndex % habits.size());
        cardHabits[1] = habits.get((currentIndex + 1) % habits.size());
        cardHabits[2] = habits.get((currentIndex + 2) % habits.size());
        cardHabits[3] = habits.get((currentIndex + 3) % habits.size());
        cardHabits[4] = habits.get((currentIndex + 4) % habits.size());
    }

    private void updateCards(Habit[] _habits) {
        progressBarLeftTwo.setProgressBarColor(_habits[0].getColourID());
        progressBarLeftTwo.setImage(ResourcesCompat.getDrawable(getResources(),
                getResId(_habits[0].getIconID(), R.drawable.class), null));
        progressBarLeftOne.setProgressBarColor(_habits[1].getColourID());
        progressBarLeftOne.setImage(ResourcesCompat.getDrawable(getResources(),
                getResId(_habits[1].getIconID(), R.drawable.class), null));
        progressBarCenter.setProgressBarColor(_habits[2].getColourID());
        progressBarCenter.setImage(ResourcesCompat.getDrawable(getResources(),
                getResId(_habits[2].getIconID(), R.drawable.class), null));
        currentHabitName.setText(_habits[2].getName());
        progressBarRightOne.setProgressBarColor(_habits[3].getColourID());
        progressBarRightOne.setImage(ResourcesCompat.getDrawable(getResources(),
                getResId(_habits[3].getIconID(), R.drawable.class), null));
        progressBarRightTwo.setProgressBarColor(_habits[4].getColourID());
        progressBarRightTwo.setImage(ResourcesCompat.getDrawable(getResources(),
                getResId(_habits[4].getIconID(), R.drawable.class), null));
    }

    public int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void swipeRight() {
        currentIndex += 1;
        updateUI();
    }

    public void swipeLeft() {
        if (currentIndex == 0) {
            currentIndex = habits.size() - 1;
        } else {
            currentIndex -= 1;
        }
        updateUI();
    }

    public void slideUpDown(final View view){
        if (!isPanelShown()) {
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_up);

            hiddenLeaderBoardOverlay.startAnimation(bottomUp);
            hiddenLeaderBoardOverlay.setVisibility(View.VISIBLE);
            hiddenLeaderBoardOverlay.setClickable(true);
            hiddenLeaderBoardOverlay.setFocusable(true);
        } else {
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_down);

            hiddenLeaderBoardOverlay.startAnimation(bottomDown);
            hiddenLeaderBoardOverlay.setVisibility(View.GONE);
        }
    }

    private boolean isPanelShown() {
        return hiddenLeaderBoardOverlay.getVisibility() == View.VISIBLE;
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