package vu.mif.habit_tracker.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import vu.mif.habit_tracker.Models.User;
import vu.mif.habit_tracker.R;

public class marketplaceFragment extends AppCompatDialogFragment implements View.OnClickListener {

    private Activity context;
    private ImageButton btn_hat;
    private TextView user_money;
    private MarketplaceListener marketplace_picker;
    private Button btn_close;
    private int money;


    public marketplaceFragment(int money)
    {
        this.money = money;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        context = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme_transparent);
        LayoutInflater inflater = context.getLayoutInflater();
        View mView = inflater.inflate(R.layout.market_layout, null);

        btn_hat = mView.findViewById(R.id.market_hat_btn);
        btn_hat.setOnClickListener(this);
        user_money = mView.findViewById(R.id.market_money);
        user_money.setText(String.valueOf(money));
        btn_close = mView.findViewById(R.id.market_closet_btn);
        btn_close.setOnClickListener(this);

        builder.setView(mView);
        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            marketplace_picker = (marketplaceFragment.MarketplaceListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString());
        }
    }

    @Override
    public void onClick(View view) {
        if(view == btn_hat)
        {
            int price = 12;
            if(money >= price) marketplace_picker.boughtItem(1, price);
            else Toast.makeText(context, "You lack " + String.valueOf(price-money) + " coins!", Toast.LENGTH_SHORT).show();
        }else if(view == btn_close)
        {
            this.dismiss();
        }

    }

    public interface MarketplaceListener
    {
        void boughtItem(int item_index, int price);
    }

}
