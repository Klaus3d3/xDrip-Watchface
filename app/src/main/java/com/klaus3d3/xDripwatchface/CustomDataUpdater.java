package com.klaus3d3.xDripwatchface;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.net.wifi.WifiManager;
import android.os.IBinder;

import android.provider.Settings;
import android.util.Log;

import com.huami.watch.transport.DataTransportResult;


import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.huami.watch.transport.DataBundle;
import com.huami.watch.transport.TransportDataItem;
import com.ingenic.iwds.slpt.SlptClockClient;
import com.kieronquinn.library.amazfitcommunication.Transporter;
import com.kieronquinn.library.amazfitcommunication.TransporterClassic;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;


import com.klaus3d3.xDripwatchface.settings.APsettings;
import com.klaus3d3.xDripwatchface.ui.xDripOtheralertActivity;
import com.klaus3d3.xDripwatchface.ui.xDripAlarmActivity;
import com.klaus3d3.xDripwatchface.widget.MainClock;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


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
    private com.klaus3d3.xDripwatchface.settings.APsettings logsave;
    private boolean sendhealthdatatoxdrip;
    private boolean lowpowermode;
    private static boolean updatetimer;

    private int counter=0;
    AlarmManager alarmManager;
    AlarmManager LowPowerManager;
    Context Settingsctx;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.w("CustomDataUpdater", "Service Created");
        alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        LowPowerManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        context=getApplicationContext();

        registerReceiver(snoozeReceiver, new IntentFilter("com.klaus3d3.xDripwatchface.snooze_alarm_intent"));
        registerReceiver(newDataEntryfromWidgetReciever, new IntentFilter("com.klaus3d3.xDripwatchface.newDataEntrytoService"));




    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            Settingsctx=getApplicationContext().createPackageContext(Constants.PACKAGE_NAME, Context.CONTEXT_IGNORE_SECURITY);
        }catch(Exception e){Log.e("xDripWidget",e.toString());}
        this.settings = new APsettings(Constants.PACKAGE_NAME, Settingsctx);
        settings.setBoolean("CustomDataUpdaterIsRunning",true);
        sendhealthdatatoxdrip=settings.getBoolean("HealthDataSwitch",false);
        updatetimer=settings.getBoolean("UpdateTimer",false);
        lowpowermode=settings.getBoolean("LowPowerMode",false);

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
                     Intent newDataUpdateIndent= new Intent("com.klaus3d3.xDripwatchface.newDataIntent");
                     newDataUpdateIndent.putExtra("DATA",db.getString("Data"));
                     sendBroadcast(newDataUpdateIndent);




            if(sendhealthdatatoxdrip){
                    DataBundle hd = new DataBundle();
                    hd.putInt("steps", StepsToSend);
                    hd.putInt("heart_rate", HRToSend);
                    hd.putInt("heart_acuracy", 1);
                    companionTransporter.send("Amazfit_Healthdata", hd);}



                        }
                if (action.equals(Constants.ACTION_XDRIP_OTHERALERT))
                {   DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();
                    String Alarmtext= getStringfromJSON("alarmtext",db.getString("Data"));

                    logsave = new APsettings(Constants.PACKAGE_NAME+".LOG", Settingsctx);
                    logsave.setString(String.valueOf(System.currentTimeMillis()),dateFormat.format(date)+" " + action.toString()+" : " +Alarmtext);
                    Intent intent = new Intent(context, xDripOtheralertActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    intent.putExtra("Data",db.getString("Data"));
                    context.startActivity(intent);



                }
                if (action.equals(Constants.ACTION_XDRIP_ALARM))
                {
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();
                    String Alarmtext= getStringfromJSON("alarmtext",db.getString("Data"));

                    logsave = new APsettings(Constants.PACKAGE_NAME+".LOG", Settingsctx);
                    logsave.setString(String.valueOf(System.currentTimeMillis()),dateFormat.format(date)+" " + action.toString()+" : " +Alarmtext);
                    Intent intent = new Intent(context, xDripAlarmActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    intent.putExtra("Data",db.getString("Data"));
                    context.startActivity(intent);

                }
                if (action.equals(Constants.ACTION_XDRIP_CANCEL))
                {    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    Date date = new Date();
                    logsave = new APsettings(Constants.PACKAGE_NAME+".LOG", Settingsctx);
                    logsave.setString(String.valueOf(System.currentTimeMillis()),dateFormat.format(date)+" " + action.toString());
                    Intent intent1 = new Intent("com.klaus3d3.xDripwatchface.close_alarm_dialog");
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

    private String getStringfromJSON(String Key, String JSON_String){
        try {
            JSONObject JSON_DATA = new JSONObject(JSON_String);
            return JSON_DATA.getString(Key);
        }catch (Exception e) {
        }
        return "";
    }

    private BroadcastReceiver snoozeReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            DataBundle db = new DataBundle();
            db.putInt("snoozetime",intent.getIntExtra("snooze_time",10));
            companionTransporter.send(Constants.ACTION_Amazfit_Snooze,db);

        }

    };

    private BroadcastReceiver newDataEntryfromWidgetReciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            DataBundle db = new DataBundle();
            db.putDouble("carbs",Double.valueOf(intent.getStringExtra("carbs")));
            db.putDouble("insulin",Double.valueOf(intent.getStringExtra("insulin")));
            db.putLong("timestamp",Long.valueOf(intent.getStringExtra("timestamp")));
            companionTransporter.send("Amazfit_Treatmentsdata",db);

        }

    };





    @Override
    public void onDestroy() {

            unregisterReceiver(snoozeReceiver);
            unregisterReceiver(newDataEntryfromWidgetReciever);

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


