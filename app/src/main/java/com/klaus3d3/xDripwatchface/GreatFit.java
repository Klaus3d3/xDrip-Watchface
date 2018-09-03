package com.klaus3d3.xDripwatchface;

import android.content.Intent;
import android.util.Log;

import com.huami.watch.watchface.AbstractSlptClock;

import com.klaus3d3.xDripwatchface.settings.APsettings;
import com.klaus3d3.xDripwatchface.widget.MainClock;
import com.klaus3d3.xDripwatchface.widget.GreatWidget;
import com.klaus3d3.xDripwatchface.widget.WeatherWidget;


/**
 * Amazfit watch faces
 */

public class GreatFit extends AbstractWatchFace {
    private Intent TransportIntent;
    private APsettings settings;


    public GreatFit() {
        super(
                new MainClock(),
                new GreatWidget(),
                new WeatherWidget()


        );
    }

    @Override
    protected Class<? extends AbstractSlptClock> slptClockClass() {
        return GreatFitSlpt.class;
    }


}