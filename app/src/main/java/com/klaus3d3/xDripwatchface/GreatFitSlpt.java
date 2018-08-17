package com.klaus3d3.xDripwatchface;

import android.content.Context;
import android.util.Log;

import com.klaus3d3.xDripwatchface.widget.BatteryWidget;
import com.klaus3d3.xDripwatchface.widget.CirclesWidget;
import com.klaus3d3.xDripwatchface.widget.HeartRateWidget;
import com.klaus3d3.xDripwatchface.widget.MainClock;
import com.klaus3d3.xDripwatchface.widget.GreatWidget;
import com.klaus3d3.xDripwatchface.widget.WeatherWidget;
import com.klaus3d3.xDripwatchface.widget.Widget;
import com.huami.watch.watchface.util.Util;
import com.ingenic.iwds.slpt.view.core.SlptAbsoluteLayout;
import com.ingenic.iwds.slpt.view.core.SlptLayout;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;

/**
 * Splt version of the watch.
 */

public class GreatFitSlpt extends AbstractWatchFaceSlpt {
    // Class variables
    private Context context;
    private boolean needRefreshSecond;
    public String greatfitParameters;

    public GreatFitSlpt() {
        super(
                new MainClock(),
                new GreatWidget(),
                new WeatherWidget(),
                //new HeartRateWidget(),
                new CirclesWidget()
        );
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
        Log.w("Klaus3d3", "Initiating watchface");

        this.greatfitParameters = "";
        //this.getResources().getBoolean(R.bool.seconds)

        this.context = this.getApplicationContext();
        this.needRefreshSecond = Util.needSlptRefreshSecond(this.context);
        if (this.needRefreshSecond) {
            this.setClockPeriodSecond(false);
        }
    }
}