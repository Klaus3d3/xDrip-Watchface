package com.klaus3d3.xDripwatchface.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import com.huami.watch.common.widget.HmHaloButton;

import android.widget.ImageView;
import android.widget.TextView;

import com.klaus3d3.xDripwatchface.CustomDataUpdater;
import com.klaus3d3.xDripwatchface.R;

public class DataEntryActivity extends Activity {
    HmHaloButton one_button,two_button,three_button,four_button,five_button,six_button,seven_button;
    HmHaloButton eight_button,nine_button,zero_button,dot_button,back_button;
    ImageView time_button, meal_button,finger_button,insulin_button;
    TextView Currenttextview;
    TextView MealTV,FingerTV,InsulinTV,TimeTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);
        one_button= (HmHaloButton) findViewById(R.id.one_button);
            one_button.setOnClickListener(ButtonClickListener);
        two_button= (HmHaloButton) findViewById(R.id.two_button);
            two_button.setOnClickListener(ButtonClickListener);
        three_button= (HmHaloButton) findViewById(R.id.three_button);
            three_button.setOnClickListener(ButtonClickListener);
        four_button= (HmHaloButton) findViewById(R.id.four_button);
            four_button.setOnClickListener(ButtonClickListener);
        five_button= (HmHaloButton) findViewById(R.id.five_button);
            five_button.setOnClickListener(ButtonClickListener);
        six_button= (HmHaloButton) findViewById(R.id.six_button);
            six_button.setOnClickListener(ButtonClickListener);
        seven_button= (HmHaloButton) findViewById(R.id.seven_button);
            seven_button.setOnClickListener(ButtonClickListener);
        eight_button= (HmHaloButton) findViewById(R.id.eight_button);
            eight_button.setOnClickListener(ButtonClickListener);
        nine_button= (HmHaloButton) findViewById(R.id.nine_button);
            nine_button.setOnClickListener(ButtonClickListener);
        zero_button= (HmHaloButton) findViewById(R.id.zero_button);
            zero_button.setOnClickListener(ButtonClickListener);
        dot_button= (HmHaloButton) findViewById(R.id.dot_button);
            dot_button.setOnClickListener(ButtonClickListener);
        back_button= (HmHaloButton) findViewById(R.id.back_button);
            back_button.setOnClickListener(ButtonClickListener);

        meal_button= (ImageView) findViewById(R.id.meal_button);
        meal_button.setOnClickListener(MealButtonClickListener);
        time_button= (ImageView) findViewById(R.id.time_button);
        time_button.setOnClickListener(TimeButtonClickListener);
        insulin_button= (ImageView) findViewById(R.id.insulin_button);
        insulin_button.setOnClickListener(InsulinButtonClickListener);
        finger_button= (ImageView) findViewById(R.id.finger_button);
        finger_button.setOnClickListener(FingerButtonClickListener);
        MealTV= (TextView) findViewById(R.id.MealTV);
        InsulinTV= (TextView) findViewById(R.id.InsulinTV);
        FingerTV= (TextView) findViewById(R.id.FingerTV);
        TimeTV= (TextView) findViewById(R.id.TimeTV);
        Currenttextview = MealTV;
        meal_button.setColorFilter(Color.WHITE);
        time_button.setColorFilter(Color.GRAY);
        insulin_button.setColorFilter(Color.GRAY);
        finger_button.setColorFilter(Color.GRAY);


    }

    private View.OnClickListener ButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            HmHaloButton button = (HmHaloButton) v;
            String oldtext = String.valueOf(Currenttextview.getText());
            if (button!=back_button)
                Currenttextview.append(button.getText());
            else if(oldtext.length()>0)  Currenttextview.setText(oldtext.substring(0,oldtext.length()-1));
        }
    };
    private View.OnClickListener MealButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            InsulinTV.setVisibility(View.INVISIBLE);
            MealTV.setVisibility(View.VISIBLE);
            TimeTV.setVisibility(View.INVISIBLE);
            FingerTV.setVisibility(View.INVISIBLE);
            meal_button.setColorFilter(Color.WHITE);
            time_button.setColorFilter(Color.GRAY);
            insulin_button.setColorFilter(Color.GRAY);
            finger_button.setColorFilter(Color.GRAY);

            Currenttextview=MealTV;

        }
    };
    private View.OnClickListener TimeButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            InsulinTV.setVisibility(View.INVISIBLE);
            MealTV.setVisibility(View.INVISIBLE);
            TimeTV.setVisibility(View.VISIBLE);
            FingerTV.setVisibility(View.INVISIBLE);
            meal_button.setColorFilter(Color.GRAY);
            time_button.setColorFilter(Color.WHITE);
            insulin_button.setColorFilter(Color.GRAY);
            finger_button.setColorFilter(Color.GRAY);
            Currenttextview=TimeTV;
        }
    };
    private View.OnClickListener FingerButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            InsulinTV.setVisibility(View.INVISIBLE);
            MealTV.setVisibility(View.INVISIBLE);
            TimeTV.setVisibility(View.INVISIBLE);
            FingerTV.setVisibility(View.VISIBLE);
            meal_button.setColorFilter(Color.GRAY);
            time_button.setColorFilter(Color.GRAY);
            insulin_button.setColorFilter(Color.GRAY);
            finger_button.setColorFilter(Color.WHITE);
            Currenttextview=FingerTV;

        }
    };
    private View.OnClickListener InsulinButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            InsulinTV.setVisibility(View.VISIBLE);
            MealTV.setVisibility(View.INVISIBLE);
            TimeTV.setVisibility(View.INVISIBLE);
            FingerTV.setVisibility(View.INVISIBLE);
            meal_button.setColorFilter(Color.GRAY);
            time_button.setColorFilter(Color.GRAY);
            insulin_button.setColorFilter(Color.WHITE);
            finger_button.setColorFilter(Color.GRAY);
            Currenttextview=InsulinTV;


        }
    };

    @Override
    public void finish() {
            Intent intent1 = new Intent("com.klaus3d3.xDripwatchface.newDataEntry");
            intent1.putExtra("Insulin",  String.valueOf(InsulinTV.getText()));
            intent1.putExtra("Meal", String.valueOf( MealTV.getText()));
            intent1.putExtra("Time",  String.valueOf(TimeTV.getText()));
            intent1.putExtra("Finger",  String.valueOf(FingerTV.getText()));
            sendBroadcast(intent1);

        super.finish();
    }

}
