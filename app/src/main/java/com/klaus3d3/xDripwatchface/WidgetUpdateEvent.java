package com.klaus3d3.xDripwatchface;
//import org.greenrobot.*;

public class WidgetUpdateEvent {
    private final String message;

    public WidgetUpdateEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
