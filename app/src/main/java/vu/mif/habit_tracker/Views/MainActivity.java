package vu.mif.habit_tracker.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.TransitionAdapter;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import vu.mif.habit_tracker.Models.Habit;
import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.R;
import vu.mif.habit_tracker.ViewModels.MainActivityViewModel;
import vu.mif.habit_tracker.components.CircularProgressBar;
import vu.mif.habit_tracker.components.HabitDialog;
import vu.mif.habit_tracker.firebaseDB;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, HabitDialog.HabitDialogListener {

    private final int USER_PICTURE_ACTIVITY = 1;
    private final int STORAGE_PERMISION_REQUEST = 3;

    private ViewGroup hiddenLeaderBoardOverlay;
    private View overlayTrigger;

    private ImageButton plusBtn;
    private ImageButton moreInfoBtn;
    private TextView currentHabitName;
    private TextView tvCurrentHabitPercentage;
    private TextView tvCurrentHabitInfo;
    private TextView tvUsername;
    private TextView tvCoins;
    private DrawerLayout drawerLayout;
    private AppCompatButton btnLogOut;
    private RelativeLayout friendListLayout;
    private ImageView ivAccountPic;
    private EditText friendsSearchEditText;
    private Button btnSearchFriends;
    private ListView friendList;
    private ArrayAdapter<String> FriendAdapter;

    private MainActivity context;
    private MotionLayout motionLayout;
    private MainActivityViewModel model;
    private HabitDialog habitDialog;

    private boolean leaderBoardOpen;
    private boolean cardsPopulated = false;

    private float startX;
    private float startY;
    private int currentHabitId;

    //Cia habitu listas ir useris
    List<Habit> habits;
    User user;

    private CircularProgressBar progressBarLeftTwo;
    private CircularProgressBar progressBarLeftOne;
    private CircularProgressBar progressBarCenter;
    private CircularProgressBar progressBarRightOne;
    private CircularProgressBar progressBarRightTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setNavigationBarColor(getResources().getColor(R.color.darkGrey, null));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        motionLayout = findViewById(R.id.motionLayout);
        context = this;
        model = new ViewModelProvider(context).get(MainActivityViewModel.class);

        plusBtn = findViewById(R.id.plusBtn);
        moreInfoBtn = findViewById(R.id.moreInfoBtn);
        drawerLayout = findViewById(R.id.accountInfoDrawer);
        hiddenLeaderBoardOverlay = findViewById(R.id.leaderboardOverlayContainer);
        overlayTrigger = findViewById(R.id.leaderboardContainer);
        currentHabitName = findViewById(R.id.currentHabitName);
        tvCurrentHabitPercentage = findViewById(R.id.tvCurrentHabitPercentage);
        tvCurrentHabitInfo = findViewById(R.id.tvCurrentHabitInfo);
        btnLogOut = findViewById(R.id.btnLogOut);
        ivAccountPic = findViewById(R.id.ivAccountPic);
        tvUsername = findViewById(R.id.tvUsername);
        tvCoins = findViewById(R.id.tvCoins);
        friendsSearchEditText = findViewById(R.id.friendsSearch_EditText);
        btnSearchFriends = findViewById(R.id.btnFriendSearch);
        friendListLayout = findViewById(R.id.friendListLayout);
        friendList = findViewById(R.id.friendList);

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
        model.getUser().observe(context, this::updateUserDetails);
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

        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                model.FriendAdapterClicked(MainActivity.this, position, friendList,FriendAdapter);
            }
        });

        // TODO: Fix onClick issue
        overlayTrigger.setOnClickListener(this);
        hiddenLeaderBoardOverlay.setOnClickListener(this);
        plusBtn.setOnClickListener(this);
        moreInfoBtn.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        ivAccountPic.setOnClickListener(this);
        btnSearchFriends.setOnClickListener(this);
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
        } else if (view == moreInfoBtn){
            checkFriendListAvailability();
            drawerLayout.openDrawer(GravityCompat.START);
        } else if (view == btnLogOut){
            model.LogOut();
            if(!model.isLoggedIn())
            {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else Toast.makeText(this, "An error occurred :(", Toast.LENGTH_SHORT).show();

        } else if (view == ivAccountPic) {
            if(firebaseDB.CheckPermission(this, STORAGE_PERMISION_REQUEST))
            {
                pickImage();
            }
        } else if(view == btnSearchFriends)
        {
            model.LookForFriends(this, friendsSearchEditText.getText().toString(), friendList, FriendAdapter);
        }
    }

    @Override
    public void applyChanges(int id, int currentProgress) {
        for (Habit habit: habits) {
            if (habit.getId() == id){
                if (!habit.isRepeatble()){
                    if (currentProgress >= habit.getTotalProgress()) {
                        user.addCurrency(10);
                        user.addPoints(10);
                        model.updateUser(user);
                        model.deleteHabit(habit);
                        habitDialog.dismiss();
                    } else {
                        habit.setCurrentProgress(currentProgress);
                        model.updateHabit(habit);
                    }
                } else {
                    if (currentProgress >= habit.getTotalProgress()) {
                        user.addCurrency(10);
                        user.addPoints(10);
                        model.updateUser(user);
                        habit.setCurrentProgress(currentProgress);
                        model.updateHabit(habit);
                        habitDialog.dismiss();
                    } else {
                        habit.setCurrentProgress(currentProgress);
                        model.updateHabit(habit);
                    }
                }
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
                    if (!leaderBoardOpen && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        if (isAClick(startX, endX, startY, endY)) {
                            if (doClickTransition()) {
                                return true;
                            }
                        }
                    }
                    break;
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
        habitDialog = new HabitDialog();
        Bundle args = new Bundle();
        for (Habit habit: habits) {
            if(habit.getId() == currentHabitId) {
                if (habit.getCurrentProgress() >= habit.getTotalProgress()){
                    return;
                }
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
        if (motionLayout.getProgress() < 0.05F && cardsPopulated) {
            openDialog();
            isClickHandled = true;
        }
        return isClickHandled;
    }

    private void updateUserDetails(User user)
    {
        this.user = user;
        tvUsername.setText(this.user.getUsername());

        tvCoins.setText(String.valueOf(this.user.getCurrency()));

        if(user.getPictureURL() != null)
        {
            File userPicture = new File(user.getPictureURL());
            if(userPicture.exists())
            {
                ivAccountPic.setImageURI(Uri.fromFile(userPicture));
            }else ivAccountPic.setImageResource(R.drawable.default_account_pic);
        }else ivAccountPic.setImageResource(R.drawable.default_account_pic);
    }

    private void resetCards(){
        progressBarLeftTwo.setProgress(0f);
        progressBarLeftOne.setProgress(0f);
        progressBarCenter.setProgress(0f);
        progressBarRightOne.setProgress(0f);
        progressBarRightTwo.setProgress(0f);
        progressBarLeftTwo.setImage(null);
        progressBarLeftOne.setImage(null);
        progressBarCenter.setImage(null);
        progressBarRightOne.setImage(null);
        progressBarRightTwo.setImage(null);
        currentHabitName.setText("");
        tvCurrentHabitPercentage.setText("Create new habit");
        tvCurrentHabitInfo.setText("");
        cardsPopulated = false;
    }

    private void updateCards(Habit[] _habits) {
        if (habits.isEmpty()){
            resetCards();
            return;
        }
        if (_habits[2] == null) {
            cardsPopulated = false;
            return;
        }
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
        tvCurrentHabitInfo.setText(String.format(Locale.ENGLISH, "%d / %d",
                centerHabit.getCurrentProgress(), centerHabit.getTotalProgress()));
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

        cardsPopulated = true;
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

            leaderBoardOpen = true;

            hiddenLeaderBoardOverlay.startAnimation(bottomUp);
            hiddenLeaderBoardOverlay.setVisibility(View.VISIBLE);
            hiddenLeaderBoardOverlay.setClickable(true);
            hiddenLeaderBoardOverlay.setFocusable(true);
        } else {
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_down);

            leaderBoardOpen = false;

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

    private void checkFriendListAvailability()
    {
        if(firebaseDB.CheckOnlineStatus(this)) friendListLayout.setVisibility(View.VISIBLE);
        else friendListLayout.setVisibility(View.INVISIBLE);
    }


    private void pickImage()
    {
        Intent chooseFile = new Intent(Intent.ACTION_PICK);
        chooseFile.setType("image/png");
        startActivityForResult(Intent.createChooser(chooseFile, "Choose a file"), USER_PICTURE_ACTIVITY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == STORAGE_PERMISION_REQUEST)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                pickImage();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == USER_PICTURE_ACTIVITY && resultCode == Activity.RESULT_OK) {
            String path = model.getPath(data.getData(), context);
            User newUser = new User(user.getUsername(), user.getCurrency(), user.getPoints(), path, user.getUID());
            newUser.setId(user.getId());
            model.updateUser(newUser);
            //Update image to Firebase
            model.UploadProfilePicture(new File(model.getPath(data.getData(), context)) , context);
        }
    }

}