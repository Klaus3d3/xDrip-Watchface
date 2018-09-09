package com.klaus3d3.xDripwatchface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Canvas;
import android.os.Handler;

import android.provider.Settings;

import android.util.Log;

import java.util.Arrays;
import java.util.LinkedList;
import android.os.PowerManager;
import com.klaus3d3.xDripwatchface.data.DataType;
import com.klaus3d3.xDripwatchface.data.MultipleWatchDataListenerAdapter;
import com.klaus3d3.xDripwatchface.data.MultipleWatchDataListener;
import com.klaus3d3.xDripwatchface.data.Xdrip;
import com.klaus3d3.xDripwatchface.settings.APsettings;
import com.klaus3d3.xDripwatchface.widget.AnalogClockWidget;
import com.klaus3d3.xDripwatchface.widget.ClockWidget;
import com.klaus3d3.xDripwatchface.widget.DigitalClockWidget;
import com.klaus3d3.xDripwatchface.widget.GreatWidget;
import com.klaus3d3.xDripwatchface.widget.MainClock;
import com.klaus3d3.xDripwatchface.widget.Widget;
import com.klaus3d3.xDripwatchface.R;
import com.github.marlonlom.utilities.timeago.TimeAgo;


import com.huami.watch.watchface.WatchDataListener;

import org.json.JSONObject;

/**
 * Abstract base class for watch faces
 */
public abstract class AbstractWatchFace extends com.huami.watch.watchface.AbstractWatchFace {


    public ClockWidget clock;
    final LinkedList<Widget> widgets = new LinkedList<>();

    private Intent TransportIntent,slptIntent;
    private APsettings settings;
    private Context Settingctx;
    public Context mContext;


    private class DigitalEngine extends com.huami.watch.watchface.AbstractWatchFace.DigitalEngine {

        private final DigitalClockWidget widget;

        public DigitalEngine(DigitalClockWidget widget) {
            this.widget = widget;
        }

        @Override
        protected void onPrepareResources(Resources resources) {
            this.widget.init(AbstractWatchFace.this);
            for (Widget widget : AbstractWatchFace.this.widgets) {
                widget.init(AbstractWatchFace.this);
                for (DataType type : widget.getDataTypes()) {
                    registerWatchDataListener(new MultipleWatchDataListenerAdapter(widget, type));
                }
            }

        }

        @Override
        protected void onDrawDigital(Canvas canvas, float width, float height, float centerX, float centerY, int seconds, int minutes, int hours, int year, int month, int day, int week, int ampm) {
            widget.onDrawDigital(canvas, width, height, centerX, centerY, seconds, minutes, hours, year, month, day, week, ampm);
            for (Widget widget : AbstractWatchFace.this.widgets) {
                canvas.translate(widget.getX(), widget.getY());
                widget.draw(canvas, width, height, centerX, centerY, minutes, hours);
                canvas.translate(-widget.getX(), -widget.getY());
            }
        }
    }

    private class AnalogEngine extends com.huami.watch.watchface.AbstractWatchFace.AnalogEngine {

        private final AnalogClockWidget widget;

        public AnalogEngine(AnalogClockWidget widget) {
            this.widget = widget;
        }

        @Override
        protected void onPrepareResources(Resources resources) {
            this.widget.init(AbstractWatchFace.this);
            for (Widget widget : AbstractWatchFace.this.widgets) {
                widget.init(AbstractWatchFace.this);
                for (DataType type : widget.getDataTypes()) {
                    registerWatchDataListener(new MultipleWatchDataListenerAdapter(widget, type));
                }
            }

        }

        @Override
        protected void onDrawAnalog(Canvas canvas, float width, float height, float centerX, float centerY, float secRot, float minRot, float hrRot) {
            widget.onDrawAnalog(canvas, width, height, centerX, centerY, secRot, minRot, hrRot);
            for (Widget widget : AbstractWatchFace.this.widgets) {
                canvas.translate(widget.getX(), widget.getY());
                widget.draw(canvas, width, height, centerX, centerY, (int)minRot, (int) hrRot);
                canvas.translate(-widget.getX(), -widget.getY());
            }
        }
    }


    protected AbstractWatchFace(ClockWidget clock, Widget... widgets) {
        this.clock = clock;
        this.widgets.addAll(Arrays.asList(widgets));
    }

    protected AbstractWatchFace() {
        //this.clock = clock;
    }

    // Status bar (ex.battery charging)
    public final Engine onCreateEngine() {
        // Show it or... show it off screen :P
        if(getResources().getBoolean(R.bool.status_bar)) {
            notifyStatusBarPosition(
                    getResources().getDimension(R.dimen.status_left),
                    getResources().getDimension(R.dimen.status_top)
            );
        }else{
            notifyStatusBarPosition(10.0F,10.0F);// not working
        }

        return AnalogClockWidget.class.isInstance(this.clock) ? new AnalogEngine((AnalogClockWidget) this.clock) : new DigitalEngine((DigitalClockWidget) this.clock);
    }
    @Override
    public void onCreate() {
       super.onCreate();
       mContext=this;
       this.slptIntent = new Intent(this, this.slptClockClass());
       this.TransportIntent = new Intent(this,CustomDataUpdater.class);
        this.startService(this.TransportIntent);
        try {
            Settingctx=getApplicationContext().createPackageContext(Constants.PACKAGE_NAME, Context.CONTEXT_IGNORE_SECURITY);
        }catch(Exception e){Log.e("xDripWidget",e.toString());}
        settings = new APsettings(Constants.PACKAGE_NAME, Settingctx);
        settings.setBoolean("WatchfaceIsRunning",true);
        this.registerReceiver(WatchfaceUpdateReceiver, new IntentFilter("com.klaus3d3.xDripwatchface.newDataIntent"));

    }
    private BroadcastReceiver WatchfaceUpdateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {


            restartSlpt();
            Log.w("xDripWatchface", "got new Data from Service "+intent.getStringExtra("DATA"));

        }

    };


    public void restartSlpt(){
        // Start Slpt
        try {
            this.stopService(this.slptIntent);
            this.startService(this.slptIntent);
            Log.w("xDripWatchFace", "Slpt service restarted" );
        }catch(Exception e){
            Log.w("xDripWatchFace", "Problem restarting slpt: "+e.toString() );
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.stopService(this.TransportIntent);
        settings = new APsettings(Constants.PACKAGE_NAME, Settingctx);
        settings.setBoolean("WatchfaceIsRunning",false);
        this.unregisterReceiver(WatchfaceUpdateReceiver);
    }


}
