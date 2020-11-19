package vu.mif.habit_tracker.ViewModels;

import android.graphics.Color;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<Integer[]> stream;

    private Integer[] colors = new Integer[5];

    public MainActivityViewModel() {
        stream = new MutableLiveData<>();
        updateData();
        updateCards();
    }

    public LiveData<Integer[]> getStream() {
        return stream;
    }

    private List<Integer> data = new ArrayList<>(Arrays.asList(
            Color.parseColor("#ffebee"),
            Color.parseColor("#ffcdd2"),
            Color.parseColor("#ef9a9a"),
            Color.parseColor("#e57373"),
            Color.parseColor("#ef5350"),
            Color.parseColor("#f44336"),
            Color.parseColor("#e53935"),
            Color.parseColor("#d32f2f"),
            Color.parseColor("#c62828"),
            Color.parseColor("#b71c1c"),
            Color.parseColor("#880e4f"),
            Color.parseColor("#ad1457"),
            Color.parseColor("#c2185b"),
            Color.parseColor("#d81b60"),
            Color.parseColor("#e91e63"),
            Color.parseColor("#ec407a"),
            Color.parseColor("#f06292"),
            Color.parseColor("#f48fb1"),
            Color.parseColor("#f8bbd0"),
            Color.parseColor("#fce4ec"),

            Color.parseColor("#fff8e1"),
            Color.parseColor("#ffecb3"),
            Color.parseColor("#ffe082"),
            Color.parseColor("#ffd54f"),
            Color.parseColor("#ffca28"),
            Color.parseColor("#ffc107"),
            Color.parseColor("#ffb300"),
            Color.parseColor("#ffa000"),
            Color.parseColor("#ff8f00"),
            Color.parseColor("#ff6f00"),

            Color.parseColor("#bf360c"),
            Color.parseColor("#d84315"),
            Color.parseColor("#e64a19"),
            Color.parseColor("#f4511e"),
            Color.parseColor("#ff5722"),
            Color.parseColor("#ff7043"),
            Color.parseColor("#ff8a65"),
            Color.parseColor("#ffab91"),
            Color.parseColor("#ffccbc"),
            Color.parseColor("#fbe9e7")
    ));

    private int currentIndex = 0;

    private void updateData() {
        colors[0] = data.get(currentIndex % data.size());
        colors[1] = data.get((currentIndex + 1) % data.size());
        colors[2] = data.get((currentIndex + 2) % data.size());
        colors[3] = data.get((currentIndex + 3) % data.size());
        colors[4] = data.get((currentIndex + 4) % data.size());
    }

    public void swipeRight() {
        currentIndex += 1;
        updateData();
        updateCards();
    }

    public void swipeLeft() {
        if (currentIndex == 0) {
            currentIndex = data.size() - 1;
        } else {
            currentIndex -= 1;
        }
        updateData();
        updateCards();
    }

    private void updateCards() {
        stream.postValue(colors);
    }
}
