package com.klaus3d3.xDripwatchface.springboard;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;


import com.klaus3d3.xDripwatchface.R;
import com.klaus3d3.xDripwatchface.CustomDataUpdater;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.klaus3d3.xDripwatchface.ui.xDripSnoozePickerActivity;


import org.json.JSONObject;

import clc.sliteplugin.flowboard.AbstractPlugin;
import clc.sliteplugin.flowboard.ISpringBoardHostStub;



public class NightscoutPage extends AbstractPlugin {

    private Context mContext;
    private Context sContext;
    private View mView;
    private boolean mHasActive = false;
    private ISpringBoardHostStub mHost = null;



    //Much like a fragment, getView returns the content view of the page. You can set up your layout here
    @Override
    public View getView(Context paramContext) {

        initIcons(paramContext);
        mContext = paramContext;
        sContext = mContext.getApplicationContext();
        mView = LayoutInflater.from(paramContext).inflate(R.layout.nightscoout_page, null);
        Button GraphButton = (Button) mView.findViewById(R.id.GraphButton);
        GraphButton.setOnClickListener(GraphButtonListener);
        Button InfoButton = (Button) mView.findViewById(R.id.InfoButton);
        InfoButton.setOnClickListener(InfoButtonListener);
        Button SetupButton = (Button) mView.findViewById(R.id.SetupUpbutton);
        SetupButton.setOnClickListener(SetupButtonListener);
        Switch ServiceSwitch = (Switch) mView.findViewById(R.id.ServiceSwitch);

        ServiceSwitch.setOnCheckedChangeListener(ServiceSwitchListener);
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


        //refreshView();
        super.onActive(paramBundle);
        //Check if the view is already inflated (reloading)
        if ((!this.mHasActive) && (this.mView != null)) {
            //It is, simply refresh

        }

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


    public void refreshView() {
        try{

        TextView sgv = (TextView) mView.findViewById(R.id.nightscout_sgv_textview);
        TextView delta = (TextView) mView.findViewById(R.id.nightscout_delta_text_view);
        TextView date = (TextView) mView.findViewById(R.id.nightscout_date_textview);
        TextView CollectionInfo = (TextView) mView.findViewById(R.id.CollectionInfo);
        TextView HardwareSourceInfo = (TextView) mView.findViewById(R.id.HardwareSourceInfo);
        TextView SensorBattery = (TextView) mView.findViewById(R.id.SensorBattery);
        TextView SensorExpires = (TextView) mView.findViewById(R.id.SensorExpires);


        ImageView graph= (ImageView)  mView.findViewById(R.id.SGVGraph);
        String parmStr1;
        //try{
        parmStr1=Settings.System.getString(mContext.getContentResolver(), "xdrip");
        // Extract data from JSON

                JSONObject json_data = new JSONObject(parmStr1);
                sgv.setText(json_data.getString("sgv"));
                delta.setText(json_data.getString("delta"));
                date.setText(TimeAgo.using(Long.valueOf(json_data.getString("timestamp"))));
                CollectionInfo.setText("Collector: " +json_data.getString("Collection_info"));
                HardwareSourceInfo.setText("Hardware Source: " +json_data.getString("hardware_source_info"));
                SensorBattery.setText("Transmitter battery: " +json_data.getString("sensor.latest_battery_level"));
                SensorExpires.setText("Expires: " +json_data.getString("sensor_expires"));

                if(!json_data.getString("widget_graph").equals("false"))
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
        mHost.getHostWindow().getContext().registerReceiver(mMessageReceiver, new IntentFilter("WidgetUpdateIntent"));



    }
    private View.OnClickListener GraphButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
          View GraphLayout = (View) mView.findViewById(R.id.GraphLayout);
          View InfoLayout = (View) mView.findViewById(R.id.InfoLayout);
          View SetupLayout = (View) mView.findViewById(R.id.SetupLayout);
          InfoLayout.setVisibility(View.INVISIBLE);
          SetupLayout.setVisibility(View.INVISIBLE);
          GraphLayout.setVisibility(View.VISIBLE);

        }
    };


    private Switch.OnCheckedChangeListener ServiceSwitchListener = new Switch.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // do something, the isChecked will be
            // true if the switch is in the On position

            if (isChecked){
                Intent TransportIntent;
                TransportIntent = new Intent( mContext,CustomDataUpdater.class);
                mHost.getHostWindow().getContext().startService(TransportIntent);
            }
            else{
                Intent TransportIntent;
                TransportIntent = new Intent( mContext,CustomDataUpdater.class);
                mHost.getHostWindow().getContext().stopService(TransportIntent);}

        }
    };


    private View.OnClickListener SetupButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            View GraphLayout = (View) mView.findViewById(R.id.GraphLayout);
            View InfoLayout = (View) mView.findViewById(R.id.InfoLayout);
            View SetupLayout = (View) mView.findViewById(R.id.SetupLayout);
            InfoLayout.setVisibility(View.INVISIBLE);
            GraphLayout.setVisibility(View.INVISIBLE);
            SetupLayout.setVisibility(View.VISIBLE);


        }
    };
    private View.OnClickListener InfoButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            View GraphLayout = (View) mView.findViewById(R.id.GraphLayout);
            View InfoLayout = (View) mView.findViewById(R.id.InfoLayout);
            View SetupLayout = (View) mView.findViewById(R.id.SetupLayout);
            SetupLayout.setVisibility(View.INVISIBLE);
            GraphLayout.setVisibility(View.INVISIBLE);
            InfoLayout.setVisibility(View.VISIBLE);

        }
    };

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.w("NightscoutPage", "Received update");
            refreshView();
        }

    };

    //Called when the page is destroyed completely (in app mode). Same as the onDestroy method of an activity
    @Override
    public void onDestroy() {
        //EventBus.getDefault().unregister(mHost.getHostWindow().getContext());
        mHost.getHostWindow().getContext().unregisterReceiver(mMessageReceiver);
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
        Switch ServiceSwitch = (Switch) mView.findViewById(R.id.ServiceSwitch);

        if (CustomDataUpdater.isServiceCreated())ServiceSwitch.setChecked(true);
        else ServiceSwitch.setChecked(false);
        refreshView();
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
