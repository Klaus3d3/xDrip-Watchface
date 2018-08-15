package com.klaus3d3.xDripwatchface;

import android.content.Context;
<<<<<<< HEAD:app/src/main/java/com/klaus3d3/xDripwatchface/GreatFitSlpt.java
import android.util.Log;

import com.klaus3d3.xDripwatchface.widget.BatteryWidget;
import com.klaus3d3.xDripwatchface.widget.CaloriesWidget;
import com.klaus3d3.xDripwatchface.widget.CirclesWidget;
import com.klaus3d3.xDripwatchface.widget.FloorWidget;
import com.klaus3d3.xDripwatchface.widget.HeartRateWidget;
import com.klaus3d3.xDripwatchface.widget.MainClock;
import com.klaus3d3.xDripwatchface.widget.GreatWidget;
import com.klaus3d3.xDripwatchface.widget.WeatherWidget;
import com.klaus3d3.xDripwatchface.widget.Widget;
=======
import android.content.Intent;
import android.util.Log;

import com.dinodevs.greatfitwatchface.settings.LoadSettings;
import com.dinodevs.greatfitwatchface.widget.BatteryWidget;
import com.dinodevs.greatfitwatchface.widget.CaloriesWidget;
import com.dinodevs.greatfitwatchface.widget.CirclesWidget;
import com.dinodevs.greatfitwatchface.widget.FloorWidget;
import com.dinodevs.greatfitwatchface.widget.HeartRateWidget;
import com.dinodevs.greatfitwatchface.widget.MainClock;
import com.dinodevs.greatfitwatchface.widget.GreatWidget;
import com.dinodevs.greatfitwatchface.widget.WeatherWidget;
import com.dinodevs.greatfitwatchface.widget.Widget;
>>>>>>> 94587dd10f0ec9e982cf95285090c7b5c382bfed:app/src/main/java/com/dinodevs/greatfitwatchface/GreatFitSlpt.java
import com.huami.watch.watchface.util.Util;
import com.ingenic.iwds.slpt.view.core.SlptAbsoluteLayout;
import com.ingenic.iwds.slpt.view.core.SlptLayout;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;

/**
 * Splt version of the watch.
 */

public class GreatFitSlpt extends AbstractWatchFaceSlpt {
<<<<<<< HEAD:app/src/main/java/com/klaus3d3/xDripwatchface/GreatFitSlpt.java
    // Class variables
    private Context context;
    private boolean needRefreshSecond;
    public String greatfitParameters;

    public GreatFitSlpt() {
        super(
                new MainClock(),
                new GreatWidget(),
                new WeatherWidget(),
                new HeartRateWidget(),
                new CirclesWidget()
        );
=======
    Context context;
    public GreatFitSlpt() {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this.getApplicationContext();

        // Load settings
        LoadSettings settings = new LoadSettings(context);

        this.clock = new MainClock(settings);

        if(settings.isCircles()) {
            this.widgets.add(new CirclesWidget(settings));
        }
        if(settings.isHeartRate()) {
            this.widgets.add(new HeartRateWidget(settings));
        }
        if(settings.isCalories()) {
            this.widgets.add(new CaloriesWidget(settings));
        }
        if(settings.isFloor()) {
            this.widgets.add(new FloorWidget(settings));
        }
        if(settings.isBattery()) {
            this.widgets.add(new BatteryWidget(settings));
        }
        if(settings.isWeather()) {
            this.widgets.add(new WeatherWidget(settings));
        }
        if(settings.isGreat()) {
            this.widgets.add(new GreatWidget(settings));
        }

        return super.onStartCommand(intent, flags, startId);
>>>>>>> 94587dd10f0ec9e982cf95285090c7b5c382bfed:app/src/main/java/com/dinodevs/greatfitwatchface/GreatFitSlpt.java
    }

    @Override
    protected SlptLayout createClockLayout26WC() {
        SlptAbsoluteLayout result = new SlptAbsoluteLayout();
        for (SlptViewComponent component : clock.buildSlptViewComponent(this)) {
            result.add(component);
        }
        for (Widget widget : widgets) {
            for (SlptViewComponent component : widget.buildSlptViewComponent(this)) {
                result.add(component);
            }
        }

        //Log.w("DinoDevs-GreatFit", "Rebuild 26WC");

        return result;
    }

    @Override
    protected SlptLayout createClockLayout8C() {
        SlptAbsoluteLayout result = new SlptAbsoluteLayout();
        for (SlptViewComponent component : clock.buildSlptViewComponent(this)) {
            result.add(component);
        }
        for (Widget widget : widgets) {
            for (SlptViewComponent component : widget.buildSlptViewComponent(this)) {
                result.add(component);
            }
        }

        //Log.w("DinoDevs-GreatFit", "Rebuild 8C");

        return result;
    }


    protected void initWatchFaceConfig() {
        Log.w("DinoDevs-GreatFit", "Initiating watchface");

<<<<<<< HEAD:app/src/main/java/com/klaus3d3/xDripwatchface/GreatFitSlpt.java
        this.greatfitParameters = "";
        //this.getResources().getBoolean(R.bool.seconds)

        this.context = this.getApplicationContext();
        this.needRefreshSecond = Util.needSlptRefreshSecond(this.context);
        if (this.needRefreshSecond) {
            this.setClockPeriodSecond(false);
=======
        //this.getResources().getBoolean(R.bool.seconds)

        Context context = this.getApplicationContext();
        boolean needRefreshSecond = Util.needSlptRefreshSecond(context);
        if (needRefreshSecond) {
            this.setClockPeriodSecond(true);
>>>>>>> 94587dd10f0ec9e982cf95285090c7b5c382bfed:app/src/main/java/com/dinodevs/greatfitwatchface/GreatFitSlpt.java
        }
    }
}