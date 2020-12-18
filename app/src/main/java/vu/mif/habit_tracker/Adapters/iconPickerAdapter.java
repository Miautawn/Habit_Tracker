package vu.mif.habit_tracker.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import vu.mif.habit_tracker.R;

public class iconPickerAdapter extends BaseAdapter {

    private Activity context;
    private int[] items;

    public iconPickerAdapter(Activity context, int[] items)
    {
        this.context = context;
        this.items = items;
    }


    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
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
            mView = inflater.inflate(R.layout.habit_icon, null);
        }
        else mView = (View)convertView;
        ImageView image = mView.findViewById(R.id.icon_image);
        image.setImageResource(items[position]);

        return mView;
    }
}
