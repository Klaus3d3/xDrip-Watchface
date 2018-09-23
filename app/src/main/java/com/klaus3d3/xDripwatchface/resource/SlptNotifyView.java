package com.klaus3d3.xDripwatchface.resource;

import com.ingenic.iwds.slpt.view.core.SlptNumView;
import com.ingenic.iwds.slpt.view.digital.SlptTimeView;

public class SlptNotifyView extends SlptNumView {
    public SlptNotifyView() {
    }

    protected short initType() {
        return SVIEW_NOTIFY;
    }
}