
package com.klaus3d3.xDripwatchface.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.WindowManager;
import android.widget.TextView;
import com.klaus3d3.xDripwatchface.R;
import android.view.View;

import org.json.JSONObject;

//import butterknife.BindView;
//import butterknife.ButterKnife;


public class xDripOtheralertActivity extends Activity {


    private Vibrator vibrator;
    private String Alarmtext_view="";
    private String SGV_view="";

    TextView sgv;
    TextView Alarmtext;


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
        setContentView(R.layout.xdripotheralertactivity);
        context = this.getApplicationContext();

        registerReceiver(mMessageReceiver, new IntentFilter("close_alarm_dialog"));
                   getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);// |
                    //WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        String Data = (getIntent().getStringExtra("Data"));
        Alarmtext_view =getStringfromJSON("alarmtext",Data);
        SGV_view= getStringfromJSON("sgv",Data);

        TextView sgv = (TextView) findViewById(R.id.SGV);
        TextView  Alarmtext = (TextView)findViewById(R.id.Alarm_text);
        sgv.setText(SGV_view);
        Alarmtext.setText(Alarmtext_view);

    }

    private String getStringfromJSON(String Key, String JSON_String){
        try {
            JSONObject JSON_DATA = new JSONObject(JSON_String);
            return JSON_DATA.getString(Key);
        }catch (Exception e) {
        }
        return "";
    }

    @Override
    public void onStart() {
        super.onStart();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrate();

    }



    private void vibrate(){if(vibrator != null) {
        long[] pattern = {0, 200, 100,200,100,200,100,200,600};
        vibrator.vibrate(pattern, 0);               };
    };

    @Override
    public void finish() {
        if(vibrator != null) {
            vibrator.cancel();                };

        super.finish();
    }

    }
