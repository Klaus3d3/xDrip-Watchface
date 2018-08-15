package com.klaus3d3.xDripwatchface.data;

import com.klaus3d3.xDripwatchface.CustomDataUpdater;

/**
 * Steps data
 */

public class Steps {

    private final int steps;
    private final int target;

    public Steps(int steps, int target) {
        this.steps = steps;
        this.target = target;
        CustomDataUpdater.StepsToSend = this.steps;
    }

    public int getSteps() {
        return steps;
    }

    public int getTarget() {
        return target;
    }


}
