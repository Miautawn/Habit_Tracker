package vu.mif.habit_tracker.components;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Locale;

import vu.mif.habit_tracker.R;

public class HabitDialog extends DialogFragment {
    private TextView tvProgress;
    private ProgressBar pbProgress;
    private EditText etUpdate;
    private ImageButton ibPlus;
    private ImageButton ibMinus;

    private int id;
    private int currentProgress;
    private int totalProgress;

    private HabitDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme_transparent);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_habit_dialog, null);

        Bundle mArgs = getArguments();
        id = mArgs.getInt("id");
        currentProgress = mArgs.getInt("current_progress");
        totalProgress = mArgs.getInt("total_progress");

        tvProgress = view.findViewById(R.id.tvProgress);
        pbProgress = view.findViewById(R.id.pbProgress);
        ibPlus = view.findViewById(R.id.ibPlus);
        ibMinus = view.findViewById(R.id.ibMinus);
        etUpdate = view.findViewById(R.id.etUpdate);

        ibPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etUpdate.getText().toString().equals("")){
                    int currentNumber = Integer.parseInt(etUpdate.getText().toString());
                    if (currentNumber >= 0){
                        currentProgress += currentNumber;
                    }
                    updateProgressBar();
                    listener.applyChanges(id, currentProgress);
                }
            }
        });

        ibMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etUpdate.getText().toString().equals("")){
                    int currentNumber = Integer.parseInt(etUpdate.getText().toString());
                    if (currentNumber >= 0){
                        if (currentProgress - currentNumber < 0) {
                            currentProgress = 0;
                        } else {
                            currentProgress -= currentNumber;
                        }
                    }
                    updateProgressBar();
                    listener.applyChanges(id, currentProgress);
                }
            }
        });

        updateProgressBar();

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (HabitDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement HabitDialogListener");
        }
    }

    public interface HabitDialogListener{
        void applyChanges(int id, int currentProgress);
    }

    private void updateProgressBar(){
        int percentage = currentProgress * 100 / totalProgress;
        if (percentage > 100) { percentage = 100; }
        pbProgress.setProgress(percentage);
        tvProgress.setText(String.format(Locale.ENGLISH, "%d / %d",
                currentProgress, totalProgress));
    }
}
