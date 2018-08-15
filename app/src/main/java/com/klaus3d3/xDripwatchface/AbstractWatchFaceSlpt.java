package com.klaus3d3.xDripwatchface;

import android.util.Log;

import com.huami.watch.watchface.AbstractSlptClock;
import com.ingenic.iwds.slpt.view.core.SlptAbsoluteLayout;
import com.ingenic.iwds.slpt.view.core.SlptLayout;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;

import java.util.Arrays;
import java.util.LinkedList;

import com.klaus3d3.xDripwatchface.widget.ClockWidget;
import com.klaus3d3.xDripwatchface.widget.Widget;

/**
 * Splt version of
 */

public abstract class AbstractWatchFaceSlpt extends AbstractSlptClock {

    final ClockWidget clock;
    final LinkedList<Widget> widgets = new LinkedList<>();

    protected AbstractWatchFaceSlpt(final ClockWidget clock, final Widget... widgets) {
        this.clock = clock;
        this.widgets.addAll(Arrays.asList(widgets));
    }
}
