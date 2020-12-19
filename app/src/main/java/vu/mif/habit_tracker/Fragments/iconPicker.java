package vu.mif.habit_tracker.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import vu.mif.habit_tracker.Adapters.iconPickerAdapter;
import vu.mif.habit_tracker.R;
import vu.mif.habit_tracker.Views.CustomHabitActivity;

public class iconPicker extends AppCompatDialogFragment {

    private Activity context;
    private iconPickerListener listener;
    public int[] items;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();

        View mView = inflater.inflate(R.layout.icon_picker_dialog, null);
        GridView gridview = mView.findViewById(R.id.icon_gridView);
        gridview.setAdapter(new iconPickerAdapter(context, items));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.applyIcon(position);
            }
        });

        builder.setView(mView);
        return  builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (iconPickerListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString());
        }
    }

    public interface iconPickerListener
    {
        void applyIcon(int position);
    }


}
