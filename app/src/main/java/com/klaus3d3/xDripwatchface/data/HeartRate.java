package com.klaus3d3.xDripwatchface.data;

import com.klaus3d3.xDripwatchface.CustomDataUpdater;

/**
 * Heart rate
 */

public class HeartRate  {

    private final int heartRate;

    public HeartRate(int heartRate) {
        this.heartRate = heartRate;
        CustomDataUpdater.HRToSend = this.heartRate;
    }

    public int getHeartRate() {
        return heartRate;
    }
}
