
package com.klaus3d3.xDripwatchface.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huami.watch.common.widget.*;


import com.klaus3d3.xDripwatchface.R;
import com.klaus3d3.xDripwatchface.springboard.CustomListAdapter;
import com.klaus3d3.xDripwatchface.springboard.MenuItems;
import com.klaus3d3.xDripwatchface.springboard.TestActivity;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;


public class xDripAlarmActivity extends Activity  implements HmWearableListView.ClickListener{


    private Vibrator vibrator;
    private String Alarmtext_view;
    private String SGV_view;
    private int snooze_time=0;
    private int default_snooze;


    private String[] mItems = {"10 Minutes", "20 Minutes", "30 Minutes", "45 Minutes", "1 Hours", "2 Hours", "3 Hours", "4 Hours", "6 Hours", "8 Hours"};
    private int[] mImages = {R.drawable.snooze_white_54x54, R.drawable.snooze_white_54x54,
            R.drawable.snooze_white_54x54, R.drawable.snooze_white_54x54,R.drawable.snooze_white_54x54, R.drawable.snooze_white_54x54
            , R.drawable.snooze_white_54x54, R.drawable.snooze_white_54x54, R.drawable.snooze_white_54x54, R.drawable.snooze_white_54x54};
    private HmTextView mHeader;
    private HmWearableListView listview;
    private View Alarm_Layout,Snooze_Layout;


    Context context;


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            finish();
        }

    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMessageReceiver);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xdripalarmactivity);
        context = this.getApplicationContext();
        listview=findViewById(R.id.WearListeview);
        Snooze_Layout=(View) findViewById(R.id.Snooze_layout);
        Alarm_Layout=(View) findViewById(R.id.Snooze_layout);
        mHeader = findViewById(R.id.header);
        loadAdapter("Select Snooze time");

        registerReceiver(mMessageReceiver, new IntentFilter("com.klaus3d3.xDripwatchface.close_alarm_dialog"));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); // |
        //&&WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        String Data = (getIntent().getStringExtra("Data"));
        Alarmtext_view = getStringfromJSON("alarmtext",Data);
        SGV_view = getStringfromJSON("sgv",Data);
        default_snooze=Integer.valueOf(getStringfromJSON("default_snooze",Data));
        TextView sgv = (TextView) findViewById(R.id.SGV);
        TextView Alarmtext = (TextView) findViewById(R.id.Alarm_text);
        sgv.setText(SGV_view);
        Alarmtext.setText(Alarmtext_view);
        Button SnoozeButton = (Button) findViewById(R.id.Snooze_Button);
        SnoozeButton.setOnClickListener(SnoozeButtonListener);


    }

    private String getStringfromJSON(String Key, String JSON_String){
        try {
            JSONObject JSON_DATA = new JSONObject(JSON_String);
              return JSON_DATA.getString(Key);
               }catch (Exception e) {
           }
        return "";
    }
    private void loadAdapter(String s) {
        mHeader.setText(s);
        List<com.klaus3d3.xDripwatchface.springboard.MenuItems> items = new ArrayList<>();
        for (int i=0; i<mItems.length; i++){
            items.add(new MenuItems(mImages[i], mItems[i]));
        }

        com.klaus3d3.xDripwatchface.springboard.CustomListAdapter mAdapter = new CustomListAdapter(this, items);

        listview.setAdapter(mAdapter);
        listview.addOnScrollListener(mOnScrollListener);
        listview.setClickListener(this);


    }

    @Override
    public void onClick(HmWearableListView.ViewHolder viewHolder) {

        switch (viewHolder.getAdapterPosition()) {
            case 0:
                snooze_time=10;
                finish();
                break;
            case 1:
                snooze_time=20;
                finish();
                break;
            case 2:
                snooze_time=30;
                finish();
                break;
            case 3:
                snooze_time=45;
                finish();
                break;
            case 4:
                snooze_time=60;
                finish();
                break;
            case 5:
                snooze_time=120;
                finish();
                break;
            case 6:
                snooze_time=180;
                finish();
                break;
            case 7:
                snooze_time=240;
                finish();
                break;
            case 8:
                snooze_time=360;
                finish();
                break;
            case 9:
                snooze_time=480;
                finish();
                break;

            default:
                Toast.makeText(this,
                        String.format("You selected item #%s",
                                viewHolder.getAdapterPosition()),
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    public void onTopEmptyRegionClick() {
        //Prevent NullPointerException
        Toast.makeText(this,
                "Top empty area tapped", Toast.LENGTH_SHORT).show();
    }

    // The following code ensures that the title scrolls as the user scrolls up
    // or down the list
    private HmWearableListView.OnScrollListener mOnScrollListener =
            new HmWearableListView.OnScrollListener() {
                @Override
                public void onAbsoluteScrollChange(int i) {
                    // Only scroll the title up from its original base position
                    // and not down.
                    if (i > 0) {
                        mHeader.setY(-i);
                    }
                }

                @Override
                public void onScroll(int i) {

                }

                @Override
                public void onScrollStateChanged(int i) {
                    // Placeholder
                }

                @Override
                public void onCentralPositionChanged(int i) {

                }
            };

    private View.OnClickListener SnoozeButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (vibrator != null) vibrator.cancel();
            Snooze_Layout.setVisibility(View.VISIBLE);
             }
    };


    @Override
    public void onStart() {
        super.onStart();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        vibrate();
    }


    private void vibrate() {
        if (vibrator != null) {
            //vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            long[] pattern = {0, 200, 100, 200, 100, 200, 100, 200, 600};
            vibrator.vibrate(pattern, 0);
        }
        ;
    }
    ;

    @Override
    public void finish() {
        if (vibrator != null) {
            vibrator.cancel();
        }
        ;

            if (snooze_time==0) snooze_time = default_snooze;
            Intent intent1 = new Intent("com.klaus3d3.xDripwatchface.snooze_alarm_intent");
            intent1.putExtra("snooze_time", snooze_time);
            sendBroadcast(intent1);

        super.finish();
    }


}
