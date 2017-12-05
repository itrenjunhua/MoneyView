package com.renj.moneyview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;

import com.renj.moneyview.weight.MoneyView;

public class MainActivity extends AppCompatActivity {

    private MoneyView moneyView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moneyView1 = (MoneyView) findViewById(R.id.moneyview1);

        moneyView1
                .setMaxLength(7)
                .setDecimalLength(3)
                .setPointCannotPosition(5)
                .setMoneyChangeListener(new MoneyView.MoneyChangeListener() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.i("MainActivity", "afterTextChanged => " + s);
                    }
                });
    }
}
