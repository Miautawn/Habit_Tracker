package vu.mif.habit_tracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.TransitionAdapter;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import vu.mif.habit_tracker.R;
import vu.mif.habit_tracker.ViewModels.MainActivityViewModel;
import vu.mif.habit_tracker.components.CircularProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewGroup hiddenLeaderBoardOverlay;
    private View overlayTrigger;

    private ImageButton plusBtn;

    MainActivity context;
    MotionLayout motionLayout;
    MainActivityViewModel model;

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

        progressBarLeftTwo = findViewById(R.id.progressBarLeftTwo);
        progressBarLeftOne = findViewById(R.id.progressBarLeftOne);
        progressBarCenter = findViewById(R.id.progressBarCenter);
        progressBarRightOne = findViewById(R.id.progressBarRightOne);
        progressBarRightTwo = findViewById(R.id.progressBarRightTwo);

        model.getStream().observe(context, this::bindProgressBar);

        motionLayout.setTransitionListener(new TransitionAdapter() {
            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                switch (currentId) {
                    case R.id.barLeft:
                        motionLayout.setProgress(0f);
                        model.swipeRight();
                        break;
                    case R.id.barRight:
                        motionLayout.setProgress(0f);
                        model.swipeLeft();
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
            //Intent intent = new Intent(MainActivity.this, NewHabitActivity.class);
            //startActivity(intent);
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

    private void bindProgressBar(Integer[] it) {
        progressBarLeftTwo.setProgressBarColor(it[0]);
        progressBarLeftOne.setProgressBarColor(it[1]);
        progressBarCenter.setProgressBarColor(it[2]);
        progressBarRightOne.setProgressBarColor(it[3]);
        progressBarRightTwo.setProgressBarColor(it[4]);
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