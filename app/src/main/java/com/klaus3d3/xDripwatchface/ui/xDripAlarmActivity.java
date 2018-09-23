
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.klaus3d3.xDripwatchface.ui.*;

import com.huami.watch.transport.DataBundle;


import com.klaus3d3.xDripwatchface.R;

import org.json.JSONObject;


public class xDripAlarmActivity extends Activity {


    private Vibrator vibrator;
    private String Alarmtext_view;
    private String SGV_view;
    private int snooze_time;
    private int default_snooze;
    private boolean snooze;
    private String Alerttype;


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

        registerReceiver(mMessageReceiver, new IntentFilter("close_alarm_dialog"));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); // |
        //&&WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        String Data = (getIntent().getStringExtra("Data"));

        Alarmtext_view = getStringfromJSON("alarmtext",Data);
        SGV_view = getStringfromJSON("sgv",Data);
        TextView sgv = (TextView) findViewById(R.id.SGV);
        TextView Alarmtext = (TextView) findViewById(R.id.Alarm_text);
        sgv.setText(SGV_view);
        Alarmtext.setText(Alarmtext_view);
        Button SnoozeButton = (Button) findViewById(R.id.Snooze_Button);
        SnoozeButton.setOnClickListener(SnoozeButtonListener);

        //default_snooze = getIntent().getIntExtra("default_snooze", 30);

    }

    private String getStringfromJSON(String Key, String JSON_String){
        try {
            JSONObject JSON_DATA = new JSONObject(JSON_String);
              return JSON_DATA.getString(Key);
               }catch (Exception e) {
           }
        return "";
    }

    private View.OnClickListener SnoozeButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (vibrator != null) vibrator.cancel();
            Intent intent = new Intent(context, xDripSnoozePickerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

            intent.putExtra("default_snooze", default_snooze);
            context.startActivity(intent);
            snooze = true;
            finish();
        }
    };


    @Override
    public void onStart() {
        super.onStart();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        snooze = false;
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
        if (!snooze) {
            snooze_time = default_snooze;
            Intent intent1 = new Intent("snooze_alarm_intent");
            intent1.putExtra("snooze_time", snooze_time);
            sendBroadcast(intent1);
        }
        super.finish();
    }


}
