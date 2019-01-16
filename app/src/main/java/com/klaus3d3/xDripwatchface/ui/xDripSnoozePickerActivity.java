
package com.klaus3d3.xDripwatchface.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.view.View;

import com.huami.watch.transport.DataBundle;
import com.klaus3d3.xDripwatchface.R;




public class xDripSnoozePickerActivity extends Activity {


    private Vibrator vibrator;
    private boolean eventBusConnected;
    private int snooze_time;
    private int default_snooze=0;
    private Button tenmin;
    private Button twenmin;
    private Button thirtymin;
    private Button fortyfivemin;
    private Button onehour;
    private Button twohour;
    private Button threehour;
    private Button fourhour;
    private Button sixhour;
    private Button eighthour;


    Context context;



    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xdripsnoozepickeractivity);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);// |
                   // WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        default_snooze=getIntent().getIntExtra("default_snooze",30);
        context = this.getApplicationContext();
        registerButtons();
    }


    @Override
    public void onStart() {
        super.onStart();

    }





    @Override
    public void finish() {

       if (snooze_time==0){snooze_time=default_snooze;}
            Intent intent1 = new Intent("com.klaus3d3.xDripwatchface.snooze_alarm_intent");
           intent1.putExtra("snooze_time",snooze_time);
           sendBroadcast(intent1);
        super.finish();
    }







   private void registerButtons() {
       Button tenmin = (Button)findViewById(R.id.tenmin);
       tenmin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              snooze_time = 10;
              finish();
           }
       });


       Button twenmin = (Button)findViewById(R.id.twenmin);
       twenmin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               snooze_time = 20;
               finish();
           }
       });
       Button thirtymin = (Button)findViewById(R.id.thirtymin);
       thirtymin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               snooze_time = 30;
               finish();
           }
       });
       Button fourtyfivemin = (Button)findViewById(R.id.fourtyfivemin);
       fourtyfivemin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               snooze_time = 45;
               finish();
           }
       });
       Button onehour = (Button)findViewById(R.id.onehour);
       onehour.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               snooze_time = 60;
               finish();
           }
       });
       Button twohour = (Button)findViewById(R.id.twohour);
       twohour.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               snooze_time = 120;
               finish();
           }
       });
       Button threehour = (Button)findViewById(R.id.threehour);
       threehour.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               snooze_time = 180;
               finish();
           }
       });
       Button fourhour = (Button)findViewById(R.id.fourhour);
       fourhour.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               snooze_time = 240;
               finish();
           }
       });
       Button sixhour = (Button) findViewById(R.id.sixhour);
       sixhour.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               snooze_time = 360;
               finish();
           }
       });
       Button eigthhour = (Button) findViewById(R.id.eighthour);
       eigthhour.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               snooze_time = 480;
               finish();
           }
       });
   }
}
