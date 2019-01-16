package com.klaus3d3.xDripwatchface.springboard;


import android.app.AlarmManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ScrollingView;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.Toast;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import android.os.SystemClock;
import android.provider.Settings;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;


import com.klaus3d3.xDripwatchface.Constants;
import com.klaus3d3.xDripwatchface.SystemProperties;
import com.klaus3d3.xDripwatchface.settings.APsettings;


import com.klaus3d3.xDripwatchface.ui.DataEntryActivity;
import com.klaus3d3.xDripwatchface.ui.xDripAlarmActivity;
import com.klaus3d3.xDripwatchface.ui.xDripSnoozePickerActivity;
import com.klaus3d3.xDripwatchface.widget.MainClock;
import com.klaus3d3.xDripwatchface.R;
import com.klaus3d3.xDripwatchface.CustomDataUpdater;
import com.github.marlonlom.utilities.timeago.TimeAgo;


import org.json.JSONObject;

import clc.sliteplugin.flowboard.AbstractPlugin;
import clc.sliteplugin.flowboard.ISpringBoardHostStub;
import com.ingenic.iwds.slpt.SlptClockClient;



public class NightscoutPage extends AbstractPlugin {
    private static String DatafromService;
    private Context mContext;
    private Context sContext;
    private View mView;
    private boolean mHasActive = false;
    private ISpringBoardHostStub mHost = null;

    private com.klaus3d3.xDripwatchface.settings.APsettings settings;
    private Button GraphButton,InfoButton,SetupButton,DataEntryButton,LogButton,SendtoxDripButton;
    private ImageView graph;
    private Switch ServiceSwitch,HealthDataSwitch,UpdateTimerSwitch,LowPowerModeSwitch;
    private TextView LogTextView,TimeTextViev,InsulinTextView,MealTextView,FingerTextView,sgv,delta,date,CollectionInfo;
    private TextView HardwareSourceInfo,SensorBattery,SensorExpires;
    View GraphLayout, InfoLayout,SetupLayout,LogLayout ,DataEntryLayout,DataField;
    private Boolean ButtonAlreadyPressed;
    Context Settingsctx;
    private SlptClockClient slptClockClient;


    //Much like a fragment, getView returns the content view of the page. You can set up your layout here

    @Override
    public View getView(Context paramContext) {

        initIcons(paramContext);
        mContext = paramContext;
        try {
        Settingsctx=mHost.getHostWindow().getContext().getApplicationContext().createPackageContext(Constants.PACKAGE_NAME, Context.CONTEXT_IGNORE_SECURITY);
        }catch(Exception e){Log.e("xDripWidget",e.toString());}

        mView = LayoutInflater.from(paramContext).inflate(R.layout.nightscoout_page, null);
        sgv = (TextView) mView.findViewById(R.id.nightscout_sgv_textview);
        delta = (TextView) mView.findViewById(R.id.nightscout_delta_text_view);
        date = (TextView) mView.findViewById(R.id.nightscout_date_textview);
        CollectionInfo = (TextView) mView.findViewById(R.id.CollectionInfo);
        HardwareSourceInfo = (TextView) mView.findViewById(R.id.HardwareSourceInfo);
        SensorBattery = (TextView) mView.findViewById(R.id.SensorBattery);
        SensorExpires = (TextView) mView.findViewById(R.id.SensorExpires);
        graph = (ImageView)  mView.findViewById(R.id.SGVGraph);
        GraphLayout = (View) mView.findViewById(R.id.GraphLayout);
        InfoLayout = (View) mView.findViewById(R.id.InfoLayout);
        SetupLayout = (View) mView.findViewById(R.id.SetupLayout);
        LogLayout = (View) mView.findViewById(R.id.LogLayout);
        DataEntryLayout = (View) mView.findViewById(R.id.DataEntryLayout);
        DataField = (View) mView.findViewById(R.id.DataField);
        GraphButton = (Button) mView.findViewById(R.id.GraphButton);
        GraphButton.setOnClickListener(GraphButtonListener);
        InfoButton = (Button) mView.findViewById(R.id.InfoButton);
        InfoButton.setOnClickListener(InfoButtonListener);
        SetupButton = (Button) mView.findViewById(R.id.SetupUpbutton);
        SetupButton.setOnClickListener(SetupButtonListener);
        SendtoxDripButton = (Button) mView.findViewById(R.id.SendtoxDripButton);
        SendtoxDripButton.setOnClickListener(SendtoxDripButtonListener);
        DataField.setOnClickListener(DataFieldClickListener);
        DataEntryButton = (Button) mView.findViewById(R.id.DataEntryButton);
        DataEntryButton.setOnClickListener(DataEntryButtonListener);
        LogButton = (Button) mView.findViewById(R.id.LogButton);
        LogButton.setOnClickListener(LogButtonListener);
        LogButton.setOnLongClickListener(ClearLogLongListener);
        ServiceSwitch = (Switch) mView.findViewById(R.id.ServiceSwitch);
        ServiceSwitch.setOnClickListener(ServiceSwitchListener);
        HealthDataSwitch = (Switch) mView.findViewById(R.id.HealthDataSwitch);
        HealthDataSwitch.setOnClickListener(HealthDataSwitchListener);
        UpdateTimerSwitch = (Switch) mView.findViewById(R.id.UpdateTimerSwitch);
        UpdateTimerSwitch.setOnClickListener(UpdateTimerSwitchListener);
        LogTextView =(TextView) mView.findViewById(R.id.LogTextView);
        TimeTextViev =(TextView) mView.findViewById(R.id.TimeTextView);
        InsulinTextView =(TextView) mView.findViewById(R.id.InsulinTextView);
        MealTextView =(TextView) mView.findViewById(R.id.MealTextView);
        FingerTextView =(TextView) mView.findViewById(R.id.FingerTextView);


        return mView;
    }





    //Return the launcher intent for this page. This might be used for the launcher as well when the page is disabled?
    @Override
    public Intent getWidgetIntent() {
        Intent localIntent = new Intent();
        /*localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        localIntent.setAction("android.intent.action.MAIN");
        localIntent.addCategory("android.intent.category.LAUNCHER");
        localIntent.setComponent(new ComponentName(this.mContext.getPackageName(), "com.huami.watch.deskclock.countdown.CountdownListActivity"));*/
        return localIntent;
    }

    //Return the title for this page, used when the page is disabled in the app list. In this case, the app name is used
    @Override
    public String getWidgetTitle(Context paramContext) {
        return this.mContext.getResources().getString(R.string.app_name);

    }

    //Called when the page is shown
    @Override
    public void onActive(Bundle paramBundle) {

        super.onActive(paramBundle);
        this.settings = new APsettings(Constants.PACKAGE_NAME, Settingsctx);

        ServiceSwitch.setChecked(settings.get("CustomDataUpdaterIsRunning",false));
        HealthDataSwitch.setChecked(settings.get("HealthDataSwitch",false));
        UpdateTimerSwitch.setChecked(settings.get("UpdateTimer",false));
        HealthDataSwitch.setEnabled(ServiceSwitch.isChecked());
        UpdateTimerSwitch.setEnabled(ServiceSwitch.isChecked());
        ButtonAlreadyPressed=false;
         //Store active state
        this.mHasActive = true;

    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }


    public void refreshView(String parmStr1) {
        try{




        //try{
        //parmStr1=Settings.System.getString(mContext.getContentResolver(), "xdrip");
        // Extract data from JSON

                JSONObject json_data = new JSONObject(parmStr1);
                sgv.setText(json_data.getString("sgv"));
                delta.setText(json_data.getString("delta"));
                date.setText(TimeAgo.using(Long.valueOf(json_data.getString("date"))));
                CollectionInfo.setText("Collector: " +json_data.getString("Collection_info"));
                HardwareSourceInfo.setText("Hardware Source: " +json_data.getString("hardware_source_info"));
                SensorBattery.setText("Transmitter battery: " +json_data.getString("sensor.latest_battery_level"));
                SensorExpires.setText("Expires: " +json_data.getString("sensor_expires"));

                if(!json_data.getString("SGVGraph").equals("false"))
                graph.setImageBitmap(StringToBitMap(json_data.getString("widget_graph")));
                else graph.setImageDrawable(mContext.getResources().getDrawable(R.drawable.empty_graph));

        }catch (Exception e) {
        Log.w("xDripWidget","Error "+e.toString());
        // Nothing
    }


           /* }catch (Exception e) {
            Log.w("xDripWidget",e.toString());
                // Nothing
            }*/
        }


    //Returns the springboard host
    public ISpringBoardHostStub getHost() {
        return mHost;
    }

    //Called when the page is loading and being bound to the host
    @Override
    public void onBindHost(ISpringBoardHostStub paramISpringBoardHostStub) {
        Log.w("xDripWidget", "onBindHost");
        mHost = paramISpringBoardHostStub;
        mHost.getHostWindow().getContext().registerReceiver(NewDatafromServiceReciever, new IntentFilter("com.klaus3d3.xDripwatchface.newDataIntent"));
        mHost.getHostWindow().getContext().registerReceiver(NewDatafromEntryDialogReciever, new IntentFilter("com.klaus3d3.xDripwatchface.newDataEntry"));

    }



    private View.OnClickListener ServiceSwitchListener = new View.OnClickListener() {
        public void onClick(View v) {

            // do something, the isChecked will be
            if (ServiceSwitch.isChecked()){
                Intent TransportIntent;
                TransportIntent = new Intent( mContext,CustomDataUpdater.class);
                mHost.getHostWindow().getContext().stopService(TransportIntent);
                mHost.getHostWindow().getContext().startService(TransportIntent);
                HealthDataSwitch.setEnabled(true);
                UpdateTimerSwitch.setEnabled(true);
                Toast.makeText(v.getContext(), "starting service", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent TransportIntent;
                TransportIntent = new Intent( mContext,CustomDataUpdater.class);
                mHost.getHostWindow().getContext().stopService(TransportIntent);
                Toast.makeText(v.getContext(), "stopping service", Toast.LENGTH_SHORT).show();
                HealthDataSwitch.setEnabled(false);
                UpdateTimerSwitch.setEnabled(false);
                }
        }
    };

    private View.OnClickListener HealthDataSwitchListener = new View.OnClickListener() {
        public void onClick(View v) {
            settings = new APsettings(Constants.PACKAGE_NAME, Settingsctx);

            if (ServiceSwitch.isChecked()&settings.get("WatchfaceIsRunning",false)){

                settings.set("HealthDataSwitch",HealthDataSwitch.isChecked());
                Intent TransportIntent;
                TransportIntent = new Intent( mContext,CustomDataUpdater.class);
                mHost.getHostWindow().getContext().stopService(TransportIntent);
                mHost.getHostWindow().getContext().startService(TransportIntent);
                Toast.makeText(v.getContext(), "restarting service", Toast.LENGTH_SHORT).show();
            }else {
                HealthDataSwitch.setChecked(false);
                Toast.makeText(v.getContext(), "Service and watchface need to run", Toast.LENGTH_SHORT).show();
            }


        }};
    private View.OnClickListener UpdateTimerSwitchListener = new View.OnClickListener() {
        public void onClick(View v) {
            settings = new APsettings(Constants.PACKAGE_NAME, Settingsctx);
            settings.set("UpdateTimer",UpdateTimerSwitch.isChecked());
            if (ServiceSwitch.isChecked()){
                Intent TransportIntent;
                TransportIntent = new Intent( mContext,CustomDataUpdater.class);
                mHost.getHostWindow().getContext().stopService(TransportIntent);
                mHost.getHostWindow().getContext().startService(TransportIntent);
                Toast.makeText(v.getContext(), "restarting service", Toast.LENGTH_SHORT).show();
            }



        }};



    private View.OnLongClickListener ClearLogLongListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {


            APsettings logsave = new APsettings(Constants.PACKAGE_NAME+".LOG", Settingsctx);
            logsave.clear();
            LogTextView.setText("");
            ButtonAlreadyPressed=true;
            return false;
        }
    };

    private View.OnClickListener SendtoxDripButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent1 = new Intent("com.klaus3d3.xDripwatchface.newDataEntrytoService");
            intent1.putExtra("insulin",  String.valueOf(InsulinTextView.getText()));
            intent1.putExtra("carbs", String.valueOf( MealTextView.getText()));
            intent1.putExtra("timestamp",  String.valueOf(TimeTextViev.getText()));
            //intent1.putExtra("Finger",  String.valueOf(FingerTV.getText()));
            mContext.sendBroadcast(intent1);


        }
    };

    private View.OnClickListener DataFieldClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            //Toast.makeText(v.getContext(), "This will start a new activity", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, DataEntryActivity.class);

           mHost.getHostWindow().getContext().startActivity(intent);


        }
    };



    private View.OnClickListener GraphButtonListener = new View.OnClickListener() {
        public void onClick(View v) {


            GraphButton.setBackground(mView.getResources().getDrawable(R.drawable.rounded_corners_button_widget_pressed));
            InfoButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));
            SetupButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));
            LogButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));
            DataEntryButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));

            GraphLayout.setVisibility(View.VISIBLE);
            InfoLayout.setVisibility(View.INVISIBLE);
            DataEntryLayout.setVisibility(View.INVISIBLE);
            SetupLayout.setVisibility(View.INVISIBLE);
            LogLayout.setVisibility(View.INVISIBLE);
        }
    };
    private View.OnClickListener SetupButtonListener = new View.OnClickListener() {
        public void onClick(View v) {

            GraphButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));
            InfoButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));
            SetupButton.setBackground(mView.getResources().getDrawable(R.drawable.rounded_corners_button_widget_pressed));
            LogButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));
            DataEntryButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));

            GraphLayout.setVisibility(View.INVISIBLE);
            InfoLayout.setVisibility(View.INVISIBLE);
            DataEntryLayout.setVisibility(View.INVISIBLE);
            SetupLayout.setVisibility(View.VISIBLE);
            LogLayout.setVisibility(View.INVISIBLE);
        }
    };
    private View.OnClickListener LogButtonListener = new View.OnClickListener() {
        public void onClick(View v) {


            GraphButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));
            InfoButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));
            SetupButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));
            LogButton.setBackground(mView.getResources().getDrawable(R.drawable.rounded_corners_button_widget_pressed));
            DataEntryButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));

            GraphLayout.setVisibility(View.INVISIBLE);
            InfoLayout.setVisibility(View.INVISIBLE);
            DataEntryLayout.setVisibility(View.INVISIBLE);
            SetupLayout.setVisibility(View.INVISIBLE);
            LogLayout.setVisibility(View.VISIBLE);
            String text="";
            String newline="";
            LogTextView.setText("");

            APsettings Logsave = new APsettings(Constants.PACKAGE_NAME+".LOG", Settingsctx);
            JSONObject Data = Logsave.getalldata();
            try{
            Iterator keysToCopyIterator = Data.keys();
            List<String> keysList = new ArrayList<String>();
            while(keysToCopyIterator.hasNext()) {

                String key = (String) keysToCopyIterator.next();
                newline=Data.getString(key)+System.lineSeparator();
                text=newline+text;
            }}catch (Exception e){e.printStackTrace();}
            LogTextView.setText(text);
            if(!ButtonAlreadyPressed)Toast.makeText(v.getContext(), "press long to clear LOG", Toast.LENGTH_SHORT).show();
            ButtonAlreadyPressed=true;

        }
    };

    private View.OnClickListener DataEntryButtonListener = new View.OnClickListener() {
        public void onClick(View v) {

            GraphButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));
            InfoButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));
            SetupButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));
            LogButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));
            DataEntryButton.setBackground(mView.getResources().getDrawable(R.drawable.rounded_corners_button_widget_pressed));

            GraphLayout.setVisibility(View.INVISIBLE);
            InfoLayout.setVisibility(View.INVISIBLE);
            DataEntryLayout.setVisibility(View.VISIBLE);
            SetupLayout.setVisibility(View.INVISIBLE);
            LogLayout.setVisibility(View.INVISIBLE);

        }
    };
    private View.OnClickListener InfoButtonListener = new View.OnClickListener() {
        public void onClick(View v) {

            GraphButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));
            InfoButton.setBackground(mView.getResources().getDrawable(R.drawable.rounded_corners_button_widget_pressed));
            SetupButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));
            LogButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));
            DataEntryButton.setBackground(mView.getResources().getDrawable(R.drawable.lang));

            GraphLayout.setVisibility(View.INVISIBLE);
            InfoLayout.setVisibility(View.VISIBLE);
            DataEntryLayout.setVisibility(View.INVISIBLE);
            SetupLayout.setVisibility(View.INVISIBLE);
            LogLayout.setVisibility(View.INVISIBLE);
        }
    };










    private BroadcastReceiver NewDatafromServiceReciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            DatafromService=intent.getStringExtra("DATA");

            Log.w("xDripWidget", "Received update");
            refreshView(DatafromService);
        }

    };
    private BroadcastReceiver NewDatafromEntryDialogReciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            TimeTextViev.setText(String.valueOf(System.currentTimeMillis()));
            InsulinTextView.setText(intent.getStringExtra("Insulin"));
            MealTextView.setText(intent.getStringExtra("Meal"));
            FingerTextView.setText(intent.getStringExtra("Finger"));
        }

    };


    //Called when the page is destroyed completely (in app mode). Same as the onDestroy method of an activity
    @Override
    public void onDestroy() {
        //EventBus.getDefault().unregister(mHost.getHostWindow().getContext());

        mHost.getHostWindow().getContext().unregisterReceiver(NewDatafromServiceReciever);
        mHost.getHostWindow().getContext().unregisterReceiver(NewDatafromEntryDialogReciever);
        super.onDestroy();

    }

    //Called when the page becomes inactive (the user has scrolled away)
    @Override
    public void onInactive(Bundle paramBundle) {
        super.onInactive(paramBundle);
        //Store active state
        this.mHasActive = false;
    }

    //Called when the page is paused (in app mode)
    @Override
    public void onPause() {
        super.onPause();
        this.mHasActive = false;
    }

    //Not sure what this does, can't find it being used anywhere. Best leave it alone
    @Override
    public void onReceiveDataFromProvider(int paramInt, Bundle paramBundle) {
        super.onReceiveDataFromProvider(paramInt, paramBundle);
    }

    //Called when the page is shown again (in app mode)
    @Override
    public void onResume() {
        Log.w("xDripWidget","onResume");

        refreshView(DatafromService);
        super.onResume();
        //Check if view already loaded
        if ((!this.mHasActive) && (this.mView != null)) {
            //It is, simply refresh
            this.mHasActive = true;

        }

        //Store active state
        this.mHasActive = true;
    }

    //Called when the page is stopped (in app mode)
    @Override
    public void onStop() {
        super.onStop();
        this.mHasActive = false;
    }



    private void initIcons(Context context) {

    }



}
