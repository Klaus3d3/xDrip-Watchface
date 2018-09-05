package com.klaus3d3.xDripwatchface;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.IBinder;

import android.provider.Settings;
import android.util.Log;

import com.huami.watch.transport.DataTransportResult;


import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.huami.watch.transport.DataBundle;
import com.huami.watch.transport.TransportDataItem;
import com.kieronquinn.library.amazfitcommunication.Transporter;
import com.kieronquinn.library.amazfitcommunication.TransporterClassic;
import android.app.AlarmManager;
import android.app.PendingIntent;


import org.json.JSONException;
import org.json.JSONObject;


import com.klaus3d3.xDripwatchface.settings.APsettings;
import com.klaus3d3.xDripwatchface.ui.xDripOtheralertActivity;
import com.klaus3d3.xDripwatchface.ui.xDripAlarmActivity;
import com.klaus3d3.xDripwatchface.widget.MainClock;


public class CustomDataUpdater extends Service {
    private Context context;



    private TransporterClassic companionTransporter;
    public static int StepsToSend=0;
    public static int HRToSend=1;
    public static Boolean bad_value= false;
    public static String watchface_color="BLACK";
    public static String watchface_strike="false";
    public static String watchface_sgv="--";
    public static String watchface_battery="--";
    public static String watchface_delta="--";
    public static String watchface_date="1";
    public static String watchface_timeago=TimeAgo.using(1);
    public static String watchface_graph="false";
    public static String widget_graph="false";
    public static String SensorExpires="";
    public static String SensorBattery="";
    public static String HardwareSourceInfo="";
    public static String CollectionInfo="";
    private com.klaus3d3.xDripwatchface.settings.APsettings settings;
    private boolean sendhealthdatatoxdrip;
    private static boolean updatetimer;
    private String agoText;
    private Intent SaveSettingIndent;
    private int minutefactor;

    private int counter=0;
    AlarmManager alarmManager;
    Context Settingsctx;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.w("CustomDataUpdater", "Service Created");
        alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        context=getApplicationContext();
        this.SaveSettingIndent= new Intent(this, TimeTick.class);
        registerReceiver(snoozeReceiver, new IntentFilter("snooze_alarm_intent"));
       try {
            Settingsctx=getApplicationContext().createPackageContext(Constants.PACKAGE_NAME, Context.CONTEXT_IGNORE_SECURITY);
        }catch(Exception e){Log.e("xDripWidget",e.toString());}


    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.settings = new APsettings(Constants.PACKAGE_NAME, Settingsctx);
        settings.setBoolean("CustomDataUpdaterIsRunning",true);
        sendhealthdatatoxdrip=settings.getBoolean("HealthDataSwitch",false);
        updatetimer=settings.getBoolean("UpdateTimer",false);

        Log.w("CustomDataUpdater", "Service started");
        if (companionTransporter == null) {
            initTransporter();
        }
                return START_STICKY;

    }



    private void initTransporter() {
        companionTransporter = (TransporterClassic) Transporter.get (this, Constants.TRANSPORTER_MODULE);
        companionTransporter.addChannelListener(new Transporter.ChannelListener() {
            @Override
            public void onChannelChanged(boolean ready) {

            }
        });
        Transporter.DataSendResultCallback test = new Transporter.DataSendResultCallback() {
            @Override
            public void onResultBack(DataTransportResult dataTransportResult) {

                Log.e("CustomDataUpdater", dataTransportResult.toString() );
            }
        };
        //companionTransporter.addServiceConnectionListener(this);
        companionTransporter.addDataListener(new Transporter.DataListener() {
            @Override
            public void onDataReceived(TransportDataItem transportDataItem) {
                String action = transportDataItem.getAction();
                DataBundle db = transportDataItem.getData();
                Log.e("CustomDataUpdater", "action: " + action + ", module: " + transportDataItem.getModuleName());

                if (action == null) {
                    return;
                }
                if (action.equals(Constants.ACTION_XDRIP_SYNC))
                {

                    watchface_date= String.valueOf(db.getLong("date"));
                    watchface_delta=db.getString("delta");
                    watchface_graph=db.getString("WFGraph");
                    widget_graph=db.getString("SGVGraph");
                    watchface_sgv=db.getString("sgv");
                    watchface_battery=db.getString("phone_battery");
                    watchface_timeago=TimeAgo.using(db.getLong("date"));
                    HardwareSourceInfo=db.getString("hardware_source_info");
                    SensorBattery=db.getString("sensor.latest_battery_level");
                    SensorExpires=db.getString("sensor_expires");
                    CollectionInfo=db.getString("Collection_info");

                    if(db.getBoolean("ishigh")||db.getBoolean("islow")||db.getBoolean("isstale")){bad_value=true;}
                    else {bad_value=false;}
                    alarmManager.cancel(PendingIntent.getBroadcast(context, 1, SaveSettingIndent, PendingIntent.FLAG_UPDATE_CURRENT));
                    if (updatetimer)
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, db.getLong("date")+1000*60, 1000*60*1,PendingIntent.getBroadcast(context, 1, SaveSettingIndent, PendingIntent.FLAG_UPDATE_CURRENT));
                    else
                    alarmManager.set(AlarmManager.RTC_WAKEUP,db.getLong("date")+1000*60*10,PendingIntent.getBroadcast(context, 1, SaveSettingIndent, PendingIntent.FLAG_UPDATE_CURRENT));

                    savetoSettings(context);
                    Intent WidgetUpdateIntent = new Intent("WidgetUpdateIntent");

                    sendBroadcast(WidgetUpdateIntent);




            if(sendhealthdatatoxdrip){
                    DataBundle hd = new DataBundle();
                    hd.putInt("steps", StepsToSend);
                    hd.putInt("heart_rate", HRToSend);
                    hd.putInt("heart_acuracy", 1);
                    companionTransporter.send("Amazfit_Healthdata", hd);}

                        }
                if (action.equals(Constants.ACTION_XDRIP_OTHERALERT))
                {
                    Intent intent = new Intent(context, xDripOtheralertActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    intent.putExtra("Alarmtext",db.getString("alarmtext"));
                    intent.putExtra("sgv",db.getString("sgv"));

                    context.startActivity(intent);



                }
                if (action.equals(Constants.ACTION_XDRIP_ALARM))
                {


                    Intent intent = new Intent(context, xDripAlarmActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    intent.putExtra("Alarmtext",db.getString("alarmtext"));
                    intent.putExtra("sgv",db.getString("sgv"));
                    intent.putExtra("default_snooze",db.getInt("default_snooze"));

                    context.startActivity(intent);

                }
                if (action.equals(Constants.ACTION_XDRIP_CANCEL))
                {   Intent intent1 = new Intent("close_alarm_dialog");
                    sendBroadcast(intent1);

                }
               // if (action.equals(Constants.ACTION_XDRIP_SNOOZE_CONFIRMATION)) Toast.makeText(context, "Snooze confirmed by watch", Toast.LENGTH_LONG).show();
            }
        });

        if (!companionTransporter.isTransportServiceConnected()) {
            Log.w("CustomDataUpdater", "connecting companionTransporter to transportService");
            companionTransporter.connectTransportService();
        }

    }


    private BroadcastReceiver snoozeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            DataBundle db = new DataBundle();
            db.putInt("snoozetime",intent.getIntExtra("snooze_time",10));
            companionTransporter.send(Constants.ACTION_Amazfit_Snooze,db);

        }

    };


    public static void savetoSettings(Context mcontext){

    String timeago;
    if (System.currentTimeMillis()-Long.valueOf(watchface_date) > 600000)
        timeago=TimeAgo.using(Long.valueOf(CustomDataUpdater.watchface_date));
    else{
        if(updatetimer)timeago=TimeAgo.using(Long.valueOf(CustomDataUpdater.watchface_date));
        else
        timeago="Less than 10 min ago";
    }
    try {
        // Extract data from JSON

        JSONObject json_data = new JSONObject();
        json_data.put("sgv",CustomDataUpdater.watchface_sgv);
        json_data.put("delta",CustomDataUpdater.watchface_delta);
        json_data.put("timestamp",CustomDataUpdater.watchface_date);
        json_data.put("timeago",timeago);
        json_data.put("sgv_graph",CustomDataUpdater.watchface_graph);
        json_data.put("widget_graph",CustomDataUpdater.widget_graph);
        json_data.put("phonebattery",CustomDataUpdater.watchface_battery);
        json_data.put("Collection_info",CustomDataUpdater.CollectionInfo);
        json_data.put("hardware_source_info",CustomDataUpdater.HardwareSourceInfo);
        json_data.put("sensor.latest_battery_level",CustomDataUpdater.SensorBattery);
        json_data.put("sensor_expires",CustomDataUpdater.SensorExpires);


        if(System.currentTimeMillis()-Long.valueOf(watchface_date) > 600000)watchface_strike="true";else watchface_strike="false";
        if(System.currentTimeMillis()-Long.valueOf(watchface_date) > 600000||CustomDataUpdater.bad_value){watchface_color="WHITE";}
        else {watchface_color="BLACK";}
        json_data.put("color",watchface_color);
        json_data.put("strike",watchface_strike);
        json_data.put("bad_value",bad_value);

        Settings.System.putString(mcontext.getApplicationContext().getContentResolver(), "xdrip",json_data.toString());

    }catch (JSONException e) {
        Log.w("CustomDataUpdater", e.toString());
    }

}


    @Override
    public void onDestroy() {

            unregisterReceiver(snoozeReceiver);
            companionTransporter.disconnectTransportService();
            unregisterComponentCallbacks(this);
            companionTransporter.removeAllChannelListeners();
            companionTransporter.removeAllDataListeners();
            companionTransporter.removeAllServiceConnectionListeners();
        this.settings = new APsettings(Constants.PACKAGE_NAME, Settingsctx);
        settings.setBoolean("CustomDataUpdaterIsRunning",false);
        Log.w("CustomDataUpdater", "Service stopped");


        super.onDestroy();
    }



}


