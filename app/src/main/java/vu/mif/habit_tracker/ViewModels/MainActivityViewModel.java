package vu.mif.habit_tracker.ViewModels;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import vu.mif.habit_tracker.Adapters.LeaderBoardAdapter;
import vu.mif.habit_tracker.Models.Habit;
import vu.mif.habit_tracker.Models.Pet;
import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.R;
import vu.mif.habit_tracker.Repositories.FireBaseRepository;
import vu.mif.habit_tracker.Repositories.HabitRepository;
import vu.mif.habit_tracker.Repositories.PetRepository;
import vu.mif.habit_tracker.Repositories.UserRepository;
import vu.mif.habit_tracker.Views.MainActivity;
import vu.mif.habit_tracker.firebaseDB;
import vu.mif.habit_tracker.roomDB;

public class MainActivityViewModel extends AndroidViewModel {
    private int currentIndex = 0;
    private MutableLiveData<Habit[]> habitCards;
    private Habit[] _habitCards = new Habit[5];

    private List<User> downloaded_users;

    private HabitRepository habitRepo;
    private FireBaseRepository fireBaseRepository;
    private LiveData<List<Habit>> habits;
    private UserRepository userRepo;
    private PetRepository petRepo;
    private LiveData<User> user;
    private LiveData<Pet> pet;
    private  List<String> friend_names;

    //Egzistencializmas man artimas
    private MainActivity mainActivity;
    public Activity context;
    public ListView friend_search;
    public ArrayAdapter<String> friendSearch_adapter;
    public ListView leaderboard;
    public LeaderBoardAdapter leaderboardAdapter;
    private String typed_username;
    public User myUser;

    private List<Habit> habitCardList;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        //Getting habits and user with livedata
        roomDB database = roomDB.getInstance(application);
        this.habitRepo = new HabitRepository(application);
        this.userRepo = new UserRepository(application);
        this.petRepo = new PetRepository(application);
        this.habits = habitRepo.getAllHabits();
        this.user = userRepo.getUser();
        this.pet = petRepo.getPet();
        this.fireBaseRepository = new FireBaseRepository(application);
        this.downloaded_users = new ArrayList<>();
        this.friend_names = new ArrayList<>();

        //sito nelieciu
        habitCards = new MutableLiveData<>();
    }

    //methods for habits
    public void insertHabit(Habit Habit) { habitRepo.insertHabit(Habit); }
    public void updateHabit(Habit Habit)
    {
        habitRepo.updateHabit(Habit);
    }
    public void deleteHabit(Habit Habit)
    {
        habitRepo.deleteHabit(Habit);
    }
    public void deleteAllHabits()
    {
        habitRepo.deleteAllHabits();
    }
    public LiveData<List<Habit>> getAllHabits()
    {
        MediatorLiveData<List<Habit>> data = new MediatorLiveData<>();
        data.addSource(habits, habits -> {
            habitCardList = habits;
            data.setValue(habits);
            updateData();
            updateCards();
        });
        return data;
    }

    //methods for user
    public void insertUser(User user) { if(this.user.getValue() == null) userRepo.insertUser(user); }
    public void updateUser(User user)
    {
        userRepo.updateUser(user);
    }
    public void deleteUser()
    {
        userRepo.deleteUser();
    }
    public LiveData<User> getUser()
    {
        return user;
    }
    public void LogOut() {userRepo.disconnectUser();}
    public boolean isLoggedIn() {return userRepo.isLogedIn();}
    public String getUID() {return  userRepo.getUID();}

    //methods for Pet
    public void updatePet(Pet pet) {petRepo.updatePet(pet);}
    public LiveData<Pet> getPet() {return pet;}
    public void insertPet(Pet pet) { petRepo.insertPet(pet); }


    public LiveData<Habit[]> getHabitCards() {
        return habitCards;
    }


    private void updateData() {
        if (habitCardList.size() != 0) {
            _habitCards[0] = habitCardList.get(currentIndex % habitCardList.size());
            _habitCards[1] = habitCardList.get((currentIndex + 1) % habitCardList.size());
            _habitCards[2] = habitCardList.get((currentIndex + 2) % habitCardList.size());
            _habitCards[3] = habitCardList.get((currentIndex + 3) % habitCardList.size());
            _habitCards[4] = habitCardList.get((currentIndex + 4) % habitCardList.size());
        }
    }

    public void swipeRight() {
        if (habitCardList != null) {
            currentIndex += 1;
            updateData();
            updateCards();
        }
    }

    public void swipeLeft() {
        if (habitCardList != null) {
            if (currentIndex == 0) {
                currentIndex = habitCardList.size() - 1;
            } else {
                currentIndex -= 1;
            }
            updateData();
            updateCards();
        }
    }

    public void updateCards() {
        habitCards.postValue(_habitCards);
    }


    public String getPath(Uri uri, Activity context) {

        String path = null;
        String[] projection = { MediaStore.Files.FileColumns.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if(cursor == null){
            path = uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }

        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }

    public void UploadProfilePicture(File image, Activity context)
    {
        UploadTask mTask = userRepo.UploadProfilePicture(Uri.fromFile(image));

        mTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(!task.isSuccessful()) Toast.makeText(context, "Could not upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void LookForFriends(String username)
    {
        typed_username = username;

        downloaded_users.clear();
        if(!typed_username.isEmpty())
        {
            if(mainActivity == null) mainActivity = (MainActivity)context;
            mainActivity.updateFriendsLoadingScreen(2);
            if(!firebaseDB.areFriendsDownloaded)
            {
                LookUpFriendIds(1);
            }else DownloadPotentialFriends();
        }
    }

    private void DownloadPotentialFriends()
    {
        Query myQuery = userRepo.DownloadPotentialFriends(typed_username);
        myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() > 0)
                {
                    for(DataSnapshot child : snapshot.getChildren())
                    {
                        if(!child.getKey().equals(userRepo.getUID()))
                        {
                            User downloaded_possible_friend = child.getValue(User.class);
                            downloaded_users.add( new User(downloaded_possible_friend.getUsername(), downloaded_possible_friend.getCurrency(), downloaded_possible_friend.getPoints(),null , child.getKey()));
                        }
                    }
                } else downloaded_users.clear();
                cleanPossibleFriends();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getMessage());
            }
        });
    }

    public void FriendAdapterClicked(Activity context,int position, ListView friendList, ArrayAdapter<String> adapter)
    {
        new AlertDialog.Builder(context).setTitle("New Friend").setMessage("Are you sure you want to add\n\"" + downloaded_users.get(position).getUsername() + "\" to friends?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                uploadNewFriendToFirebase(downloaded_users.get(position));
                firebaseDB.Friends.add(downloaded_users.get(position));
                cleanPossibleFriends();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void cleanPossibleFriends()
    {
        if(downloaded_users.size() != 0 && firebaseDB.Friends.size() != 0)
        {
            int size = downloaded_users.size();
            for(int i = 0; i<size; i++)
            {
                for(int j = 0; j< firebaseDB.Friends.size(); j++)
                {
                    if(downloaded_users.get(0).getUID().equals(firebaseDB.Friends.get(j).getUID()))
                    {
                        downloaded_users.remove(0);
                        break;
                    }
                }
            }
        }
        updateFriend_search();
    }

    private void updateFriend_search()
    {
        friend_names.clear();
        if(friendSearch_adapter == null)
        {
            friendSearch_adapter = new ArrayAdapter<String>(context, R.layout.friendlist_item, friend_names);
            friend_search.setAdapter(friendSearch_adapter);
        }
        for(int i = 0; i<downloaded_users.size(); i++) friend_names.add(downloaded_users.get(i).getUsername());

        if(downloaded_users.size() > 0) mainActivity.updateFriendsLoadingScreen(3);
        else mainActivity.updateFriendsLoadingScreen(4);
        friendSearch_adapter.notifyDataSetChanged();
    }

    private void uploadNewFriendToFirebase(User user)
    {
       DatabaseReference myRef = userRepo.UploadNewFriend(user.getUID());
       myRef.setValue(user.getUID(), new DatabaseReference.CompletionListener() {
           @Override
           public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
               if(error != null) System.out.println(error.getMessage());
           }
       });
    }

    private void LookUpFriendIds(int CODE)
    {
        Query myQuery = userRepo.GetFriendIds();
        List<String> friendIds = new ArrayList<>();
        myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() > 0)
                {
                    for(DataSnapshot id : snapshot.getChildren())
                    {
                        friendIds.add(id.getKey());
                    }
                    firebaseDB.areFriendsDownloaded = true;
                }
                switch (CODE)
                {
                    case 1:
                        if(snapshot.getChildrenCount() > 0) DownloadFriends(friendIds, userRepo.DownloadUser(friendIds.get(0)), CODE);
                        else  DownloadPotentialFriends();
                        break;
                    case 2:
                        if(snapshot.getChildrenCount() > 0) DownloadFriends(friendIds, userRepo.DownloadUser(friendIds.get(0)), CODE);
                        else SortLeaderboard();
                        break;

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getMessage());
            }
        });
    }

    private void DownloadFriends(List<String> FriendIds, DatabaseReference myRef, int CODE)
    {
        List<String> myIds = new ArrayList<>(FriendIds);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User downloaded_friend = snapshot.getValue(User.class);
                firebaseDB.Friends.add(new User(downloaded_friend.getUsername(), downloaded_friend.getCurrency(), downloaded_friend.getPoints(), null, snapshot.getKey()));
                myIds.remove(0);
                if(myIds.size() != 0) DownloadFriends(myIds, userRepo.DownloadUser(myIds.get(0)), CODE);
                else
                {
                    switch (CODE)
                    {
                        case 1:
                            DownloadPotentialFriends();
                            break;
                        case 2:
                            downloadFriendImages(firebaseDB.Friends, firebaseDB.getStorageInstance().getReference().child("UserImages"));
                            break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getMessage());
            }
        });
    }

    private void downloadFriendImages(List<User> friends, StorageReference myRef)
    {
        List<User> tempFriends = new ArrayList<>(friends);
       StorageReference tempRef = myRef.child(tempFriends.get(0).getUID());
       tempRef.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
           @Override
           public void onSuccess(byte[] bytes) {
               Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                firebaseDB.FriendImages.add(bmp);
                tempFriends.remove(0);
                if(tempFriends.size() != 0)
                {
                   downloadFriendImages(tempFriends, firebaseDB.getStorageInstance().getReference().child("UserImages"));
                }else{
                    SortLeaderboard();
                }
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               System.out.println(e.getMessage());
               firebaseDB.FriendImages.add(null);
               tempFriends.remove(0);
               if(tempFriends.size() != 0)
               {
                   downloadFriendImages(tempFriends, firebaseDB.getStorageInstance().getReference().child("UserImages"));
               }else{
                   SortLeaderboard();
               }
           }
       });
    }

    public void UpdateLeaderBoard()
    {
        if(mainActivity == null) mainActivity = (MainActivity)context;
        mainActivity.updateLeaderboardLoadingScreen(1);
        firebaseDB.Friends.clear();
        firebaseDB.FriendImages.clear();
        LookUpFriendIds(2);

    }

    private void SortLeaderboard()
    {
        List<User> friends = new ArrayList<>(firebaseDB.Friends);
        friends.add(myUser);
        List<Bitmap> images = new ArrayList<>(firebaseDB.FriendImages);
        try
        {
            File userPicture = new File(myUser.getPictureURL());
            Bitmap temp_bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.fromFile(userPicture));
            images.add(temp_bmp);
        }catch (Exception e)
        {
            images.add(null);
        }


     for(int i = 0; i<friends.size()-1; i++)
     {
         for(int j = i+1; j<friends.size(); j++)
         {
           if(friends.get(j).getPoints() > friends.get(i).getPoints())
           {
                User tempUser = friends.get(i);
                friends.set(i, friends.get(j));
                friends.set(j, tempUser);

                Bitmap tempBmp = images.get(i);
                images.set(i, images.get(j));
                images.set(j, tempBmp);
           }
         }
     }
     firebaseDB.Friends = friends;
     firebaseDB.FriendImages = images;
     updateLeaderBoardAdapter();
    }

    private void updateLeaderBoardAdapter()
    {
        if(firebaseDB.Friends.size() != 1) mainActivity.updateLeaderboardLoadingScreen(2);
        else mainActivity.updateLeaderboardLoadingScreen(3);
        leaderboardAdapter = new LeaderBoardAdapter(context, firebaseDB.Friends, firebaseDB.FriendImages);
        leaderboard.setAdapter(leaderboardAdapter);
    }
}
