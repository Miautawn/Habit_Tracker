package vu.mif.habit_tracker.ViewModels;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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

import vu.mif.habit_tracker.Models.Habit;
import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.R;
import vu.mif.habit_tracker.Repositories.FireBaseRepository;
import vu.mif.habit_tracker.Repositories.HabitRepository;
import vu.mif.habit_tracker.Repositories.UserRepository;
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
    private LiveData<User> user;

    private List<Habit> habitCardList;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        //Getting habits and user with livedata
        roomDB database = roomDB.getInstance(application);
        habitRepo = new HabitRepository(application);
        userRepo = new UserRepository(application);
        habits = habitRepo.getAllHabits();
        user = userRepo.getUser();
        fireBaseRepository = new FireBaseRepository(application);
        downloaded_users = new ArrayList<>();
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


//    public String GetAndCopyImage(Activity context, Intent data)
//    {
//        Uri uri = data.getData();
//        File source = new File(getPath(uri, context));
//        String filename = source.getName();
//
//        File destination_path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TackleData/UserPicture/");
//        File destination = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/TackleData/UserPicture/" + filename);
//
//        boolean success = true;
//        try
//        {
//            checkDestination(destination_path, context);
//            copy(source, destination, context);
//        }catch (Exception e)
//        {
//            System.out.println(e.getMessage());
//            success = false;
//        }
//        if(success) return destination.getAbsolutePath();
//        return null;
//    }

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

//    private void copy(File source, File destination, Activity context) throws IOException {
//            FileChannel in = new FileInputStream(source).getChannel();
//            FileChannel out = new FileOutputStream(destination).getChannel();
//            in.transferTo(0, in.size(), out);
//            if (in != null)
//                in.close();
//            if (out != null)
//                out.close();
//    }
//

//    private void checkDestination(File destination, Activity context)
//    {
//        if(!destination.isDirectory())
//        {
//            if(!destination.mkdirs())
//            {
//                Toast.makeText(context, "Failed to create a directory", Toast.LENGTH_LONG).show();
//            }
//        }
//    }

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

    public void LookForFriends(Activity context, String typed_username, ListView friendList, ArrayAdapter<String> adapter)
    {
        downloaded_users.clear();
        if(!typed_username.isEmpty())
        {
            //download olf friends, if not yet downloaded
            if(!firebaseDB.areFriendsDownloaded)
            {
                Query myQuery = userRepo.DownloadFriends();
                myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getChildrenCount() > 0)
                        {
                            for(DataSnapshot child : snapshot.getChildren())
                            {
                                User downloaded_friend = child.getValue(User.class);
                                firebaseDB.Friends.add(new User(downloaded_friend.getUsername(), downloaded_friend.getCurrency(), downloaded_friend.getPoints(),null, child.getKey()));
                            }
                            firebaseDB.areFriendsDownloaded = true;
                        }
                        DownloadNewFriends(context, typed_username, friendList, adapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println(error.getMessage());
                    }
                });
            } else DownloadNewFriends(context, typed_username, friendList, adapter);
        }
    }

    private void DownloadNewFriends(Activity context, String typed_username, ListView friendList, ArrayAdapter<String> adapter)
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
                cleanPossibleFriends(context, adapter, friendList);
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

                uploadUserToFirebase(downloaded_users.get(position));
                firebaseDB.Friends.add(downloaded_users.get(position));
                cleanPossibleFriends(context, adapter, friendList);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void cleanPossibleFriends(Activity context, ArrayAdapter<String> adapter, ListView friendList)
    {
        if(downloaded_users.size() != 0 && firebaseDB.Friends.size() != 0)
        {
            for(int i = 0; i<downloaded_users.size(); i++)
            {
                for(int j = 0; j< firebaseDB.Friends.size(); j++)
                {
                    if(downloaded_users.get(i).getUID().equals(firebaseDB.Friends.get(j).getUID())) downloaded_users.remove(i);
                }
            }
        }
        updateAdapter(context, adapter, friendList);
    }

    private void updateAdapter(Activity context, ArrayAdapter<String> adapter, ListView friendList)
    {
        List<String> friend_names = new ArrayList<>();
        for(int i = 0; i<downloaded_users.size(); i++) friend_names.add(downloaded_users.get(i).getUsername());
        if(adapter == null)
        {
            adapter = new ArrayAdapter<String>(context, R.layout.friendlist_item, friend_names);
            friendList.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }

    private void uploadUserToFirebase(User user)
    {
       DatabaseReference myRef = userRepo.UploadNewFriend(user.getUID());
       myRef.setValue(user, new DatabaseReference.CompletionListener() {
           @Override
           public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
               if(error == null)
               {

               }else System.out.println(error.getMessage());
           }
       });
    }
}
