package vu.mif.habit_tracker.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.R;
import vu.mif.habit_tracker.Repositories.UserRepository;

public class LeaderBoardAdapter extends BaseAdapter {
    private Activity context;
    private List<User> friends;
    private List<Bitmap> friendImages;

    public LeaderBoardAdapter(Activity context, List<User> friends, List<Bitmap> friendImages)
    {
        this.context = context;
        this.friends = friends;
        this.friendImages = friendImages;
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView;
        if(convertView == null)
        {
            mView = new View(context);
            LayoutInflater inflater = context.getLayoutInflater();
            mView = inflater.inflate(R.layout.leaderboard_element, null);
        }
        else mView = (View)convertView;
        ImageView userProfile = mView.findViewById(R.id.leaderboard_userImage);
        TextView userName = mView.findViewById(R.id.leaderboard_userName);
        userName.setText(friends.get(position).getUsername());
        TextView userScore = mView.findViewById(R.id.leaderboard_userScore);
        userScore.setText(String.valueOf(friends.get(position).getPoints()));
        if (friendImages.get(position) == null)
        {
            userProfile.setImageResource(R.drawable.default_account_pic);
        }else userProfile.setImageBitmap(friendImages.get(position));

        return mView;
    }
}
