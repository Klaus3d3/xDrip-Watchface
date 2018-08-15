package com.klaus3d3.xDripwatchface.resource;

import com.ingenic.iwds.slpt.view.digital.SlptTimeView;

public class SlptNotifyView extends SlptTimeView {
    public SlptNotifyView() {
    }

    protected short initType() {
        return SVIEW_NOTIFY;
    }
}