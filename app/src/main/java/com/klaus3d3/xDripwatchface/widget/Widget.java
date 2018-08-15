package com.klaus3d3.xDripwatchface.widget;

import android.app.Service;
import android.graphics.Canvas;

import com.klaus3d3.xDripwatchface.data.MultipleWatchDataListener;


public interface Widget extends MultipleWatchDataListener, HasSlptViewComponent {

    int getX();

    int getY();

    void setX(int x);

    void setY(int y);

    void init(Service service);

    void draw(Canvas canvas, float width, float height, float centerX, float centerY, int minutes, int hours);
}
