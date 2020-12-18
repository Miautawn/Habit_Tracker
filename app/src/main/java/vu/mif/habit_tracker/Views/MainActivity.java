package vu.mif.habit_tracker.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.TransitionAdapter;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import vu.mif.habit_tracker.Models.Habit;
import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.R;
import vu.mif.habit_tracker.ViewModels.MainActivityViewModel;
import vu.mif.habit_tracker.components.CircularProgressBar;
import vu.mif.habit_tracker.components.HabitDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, HabitDialog.HabitDialogListener {

    private ViewGroup hiddenLeaderBoardOverlay;
    private View overlayTrigger;

    private ImageButton plusBtn;
    private TextView currentHabitName;
    private TextView tvCurrentHabitPercentage;
    private TextView tvCurrentHabitInfo;

    MainActivity context;
    MotionLayout motionLayout;
    MainActivityViewModel model;

    float startX;
    float startY;
    int currentHabitId;

    //Cia habitu listas ir useris
    List<Habit> habits;
    User user;

    CircularProgressBar progressBarLeftTwo;
    CircularProgressBar progressBarLeftOne;
    CircularProgressBar progressBarCenter;
    CircularProgressBar progressBarRightOne;
    CircularProgressBar progressBarRightTwo;

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
        tvCurrentHabitPercentage = findViewById(R.id.tvCurrentHabitPercentage);
        tvCurrentHabitInfo = findViewById(R.id.tvCurrentHabitInfo);

        progressBarLeftTwo = findViewById(R.id.progressBarLeftTwo);
        progressBarLeftOne = findViewById(R.id.progressBarLeftOne);
        progressBarCenter = findViewById(R.id.progressBarCenter);
        progressBarRightOne = findViewById(R.id.progressBarRightOne);
        progressBarRightTwo = findViewById(R.id.progressBarRightTwo);

        model.getAllHabits().observe(context, new Observer<List<Habit>>() {
            @Override
            public void onChanged(List<Habit> habits) {
                context.habits = habits;
            }
        });
        model.getUser().observe(context, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                context.user = user;
            }
        });
        model.getHabitCards().observe(context, this::updateCards);

        motionLayout.setTransitionListener(new TransitionAdapter() {
            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                switch (currentId) {
                    case R.id.barLeft:
                        model.swipeRight();
                        motionLayout.setProgress(0f);
                        break;
                    case R.id.barRight:
                        model.swipeLeft();
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

    @Override
    public void applyChanges(int id, int currentProgress) {
        for (Habit habit: habits) {
            if (habit.getId() == id){
                habit.setCurrentProgress(currentProgress);
                model.updateHabit(habit);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (touchEventInsideTargetView(progressBarCenter, ev)) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = ev.getX();
                    startY = ev.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    float endX = ev.getX();
                    float endY = ev.getY();
                    if (isAClick(startX, endX, startY, endY)) {
                        if(doClickTransition()){
                            return true;
                        }
                    }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean touchEventInsideTargetView(View v, MotionEvent ev) {
        if (ev.getX() > v.getLeft() && ev.getX() < v.getRight()) {
            if (ev.getY() > v.getTop() && ev.getY() < v.getBottom()) {
                return true;
            }
        }
        return false;
    }

    private boolean isAClick(Float startX, Float endX, Float startY, Float endY){
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        return !/* =5 */(differenceX > 200 || differenceY > 200);
    }

    public void openDialog() {
        HabitDialog habitDialog = new HabitDialog();
        Bundle args = new Bundle();
        for (Habit habit: habits) {
            if(habit.getId() == currentHabitId) {
                args.putInt("id", currentHabitId);
                args.putInt("total_progress", habit.getTotalProgress());
                args.putInt("current_progress", habit.getCurrentProgress());
            }
        }
        habitDialog.setArguments(args);
        habitDialog.show(getSupportFragmentManager(), "habit dialog");
    }

    private boolean doClickTransition(){
        boolean isClickHandled = false;
        if (motionLayout.getProgress() < 0.05F) {
            openDialog();
            isClickHandled = true;
        }
        return isClickHandled;
    }

    private void updateCards(Habit[] _habits) {
        if (_habits[2] == null) return;
        Habit leftTwoHabit = _habits[0];
        progressBarLeftTwo.setProgressBarColor(leftTwoHabit.getColourID());
        progressBarLeftTwo.setImage(ResourcesCompat.getDrawable(getResources(),
                getResId(leftTwoHabit.getIconID(), R.drawable.class), null));
        int leftTwoPercentage = (leftTwoHabit.getCurrentProgress() * 100 / leftTwoHabit.getTotalProgress());
        progressBarLeftTwo.setProgress(leftTwoPercentage);

        Habit leftOneHabit = _habits[1];
        progressBarLeftOne.setProgressBarColor(leftOneHabit.getColourID());
        progressBarLeftOne.setImage(ResourcesCompat.getDrawable(getResources(),
                getResId(leftOneHabit.getIconID(), R.drawable.class), null));
        int leftOnePercentage = (leftOneHabit.getCurrentProgress() * 100 / leftOneHabit.getTotalProgress());
        progressBarLeftOne.setProgress(leftOnePercentage);

        // Set up current progressBar info
        Habit centerHabit = _habits[2];
        currentHabitId = centerHabit.getId();
        progressBarCenter.setProgressBarColor(centerHabit.getColourID());
        progressBarCenter.setImage(ResourcesCompat.getDrawable(getResources(),
                getResId(centerHabit.getIconID(), R.drawable.class), null));
        currentHabitName.setText(centerHabit.getName());

        int currentPercentage = (centerHabit.getCurrentProgress() * 100 / centerHabit.getTotalProgress());
        tvCurrentHabitPercentage.setText(String.format(Locale.ENGLISH, "%d%%",
                currentPercentage));
        tvCurrentHabitInfo.setText(String.format(Locale.ENGLISH, "%d / %d %s",
                centerHabit.getCurrentProgress(), centerHabit.getTotalProgress(), "pages"));
        progressBarCenter.setProgress(currentPercentage);

        Habit rightOneHabit = _habits[3];
        progressBarRightOne.setProgressBarColor(rightOneHabit.getColourID());
        progressBarRightOne.setImage(ResourcesCompat.getDrawable(getResources(),
                getResId(rightOneHabit.getIconID(), R.drawable.class), null));
        int rightOnePercentage = (rightOneHabit.getCurrentProgress() * 100 / rightOneHabit.getTotalProgress());
        progressBarRightOne.setProgress(rightOnePercentage);

        Habit rightTwoHabit = _habits[4];
        progressBarRightTwo.setProgressBarColor(rightTwoHabit.getColourID());
        progressBarRightTwo.setImage(ResourcesCompat.getDrawable(getResources(),
                getResId(rightTwoHabit.getIconID(), R.drawable.class), null));
        int rightTwoPercentage = (rightTwoHabit.getCurrentProgress() * 100 / rightTwoHabit.getTotalProgress());
        progressBarRightTwo.setProgress(rightTwoPercentage);
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