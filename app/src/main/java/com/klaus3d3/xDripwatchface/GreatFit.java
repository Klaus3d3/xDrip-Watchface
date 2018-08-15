package com.klaus3d3.xDripwatchface;

import com.huami.watch.watchface.AbstractSlptClock;

import com.klaus3d3.xDripwatchface.widget.BatteryWidget;
import com.klaus3d3.xDripwatchface.widget.CirclesWidget;
import com.klaus3d3.xDripwatchface.widget.HeartRateWidget;
import com.klaus3d3.xDripwatchface.widget.MainClock;
import com.klaus3d3.xDripwatchface.widget.CaloriesWidget;
import com.klaus3d3.xDripwatchface.widget.FloorWidget;
import com.klaus3d3.xDripwatchface.widget.GreatWidget;
import com.klaus3d3.xDripwatchface.widget.WeatherWidget;


/**
 * Amazfit watch faces
 */

public class GreatFit extends AbstractWatchFace {

    public GreatFit() {
        super(
                new MainClock(),
                new GreatWidget(),
                new WeatherWidget(),
                new HeartRateWidget(),
                new CirclesWidget()

        );
    }

    @Override
    protected Class<? extends AbstractSlptClock> slptClockClass() {
        return GreatFitSlpt.class;
    }
}