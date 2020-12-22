package vu.mif.habit_tracker.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.R;

public class LeaderBoardAdapter extends BaseAdapter {
    private Activity context;
    private List<User> friends;

    public LeaderBoardAdapter(Activity context, List<User> friends)
    {
        this.context = context;
        this.friends = friends;
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
        TextView userScore = mView.findViewById(R.id.leaderboard_userScore);
        //if(user has no image dsadfa)
        return mView;
    }
}
