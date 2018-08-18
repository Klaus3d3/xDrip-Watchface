package com.klaus3d3.xDripwatchface.widget;

import android.app.Service;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.huami.watch.watchface.util.Util;
import com.ingenic.iwds.slpt.view.arc.SlptPowerArcAnglePicView;
import com.ingenic.iwds.slpt.view.arc.SlptTodayDistanceArcAnglePicView;
import com.ingenic.iwds.slpt.view.arc.SlptTodayStepArcAnglePicView;
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout;
import com.ingenic.iwds.slpt.view.core.SlptPictureView;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;
import com.ingenic.iwds.slpt.view.sport.SlptPowerNumView;
import com.ingenic.iwds.slpt.view.sport.SlptTodaySportDistanceFView;
import com.ingenic.iwds.slpt.view.sport.SlptTodaySportDistanceLView;
import com.ingenic.iwds.slpt.view.sport.SlptTodayStepNumView;
import com.ingenic.iwds.slpt.view.sport.SlptTotalDistanceFView;
import com.ingenic.iwds.slpt.view.sport.SlptTotalDistanceLView;
import com.ingenic.iwds.slpt.view.utils.SimpleFile;

import java.util.Arrays;
import java.util.List;

import com.klaus3d3.xDripwatchface.R;
import com.klaus3d3.xDripwatchface.data.Battery;
import com.klaus3d3.xDripwatchface.data.DataType;
import com.klaus3d3.xDripwatchface.data.Steps;
import com.klaus3d3.xDripwatchface.data.TodayDistance;
import com.klaus3d3.xDripwatchface.data.TotalDistance;
import com.klaus3d3.xDripwatchface.resource.ResourceManager;

public class CirclesWidget extends AbstractWidget {



    private Paint batteryPaint;
    private Paint stepsPaint;

    private boolean batteryBool;
    private boolean stepsBool;

    private boolean batteryAlignLeftBool;
    private boolean stepsAlignLeftBool;


    private Battery batteryData;


    private Steps stepsData;





    private float batteryTextLeft;
    private float batteryTextTop;
    private float stepsTextLeft;
    private float stepsTextTop;


    @Override
    public void init(Service service) {
        // Get widget text show/hide booleans
        this.batteryBool = service.getResources().getBoolean(R.bool.battery);
        this.stepsBool = service.getResources().getBoolean(R.bool.steps);


        // Aling left true or false (false= align center)
        this.batteryAlignLeftBool = service.getResources().getBoolean(R.bool.battery_left_align);
        this.stepsAlignLeftBool = service.getResources().getBoolean(R.bool.steps_left_align);


        // Widgets text colors
        this.batteryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.batteryPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MONO_SPACE));
        this.batteryPaint.setTextSize(service.getResources().getDimension(R.dimen.steps_font_size));
        this.batteryPaint.setColor(service.getResources().getColor(R.color.battery_colour));
        this.batteryPaint.setTextAlign( (this.batteryAlignLeftBool) ? Paint.Align.LEFT : Paint.Align.CENTER );

        this.stepsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.stepsPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MONO_SPACE));
        this.stepsPaint.setTextSize(service.getResources().getDimension(R.dimen.steps_font_size));
        this.stepsPaint.setColor(service.getResources().getColor(R.color.steps_colour));
        this.stepsPaint.setTextAlign( (this.stepsAlignLeftBool) ? Paint.Align.LEFT : Paint.Align.CENTER );


        // Text positions
        this.batteryTextLeft = service.getResources().getDimension(R.dimen.battery_text_left);
        this.batteryTextTop = service.getResources().getDimension(R.dimen.battery_text_top);
        this.stepsTextLeft = service.getResources().getDimension(R.dimen.steps_text_left);
        this.stepsTextTop = service.getResources().getDimension(R.dimen.steps_text_top);




    }

    @Override
    public void draw(Canvas canvas, float width, float height, float centerX, float centerY, int minutes, int hours) {

        // Show battery
        if (this.batteryData != null && this.batteryBool) {
            String text = String.format("%02d%%", this.batteryData.getLevel() * 100 / this.batteryData.getScale());
            canvas.drawText(text, batteryTextLeft, batteryTextTop, batteryPaint);
        }

        // Show steps
        if (this.stepsData != null && this.stepsBool) {
            String text = String.format("%s", this.stepsData.getSteps());
            canvas.drawText(text, stepsTextLeft, stepsTextTop, stepsPaint);
        }




    }

    @Override
    public void onDataUpdate(DataType type, Object value) {
        switch (type) {
            case STEPS:
                onSteps((Steps) value);
                break;
            case BATTERY:
                onBatteryData((Battery) value);
                break;

        }
    }

    @Override
    public List<DataType> getDataTypes() {
        return Arrays.asList(DataType.BATTERY, DataType.STEPS);
    }

    // Update steps variables
    private void onSteps(Steps steps) {
        this.stepsData = steps;

    }

    // Update battery variables
    private void onBatteryData(Battery battery) {
        this.batteryData = battery;

    }






    // SLTP mode (Screen off)
    @Override
    public List<SlptViewComponent> buildSlptViewComponent(Service service) {
        // Variables

        int tmp_left;

        // It's a bird, it's a plane... nope... it's a font.
        Typeface timeTypeFace = ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MONO_SPACE);

        // Show battery
        SlptLinearLayout power = new SlptLinearLayout();
        SlptPictureView percentage = new SlptPictureView();
        percentage.setStringPicture("%");
        power.add(new SlptPowerNumView());
        power.add(percentage);
        power.setTextAttrForAll(
                service.getResources().getDimension(R.dimen.battery_font_size),
                service.getResources().getColor(R.color.battery_colour_slpt),
                timeTypeFace
        );
        // Position based on screen on
        power.alignX = 2;
        power.alignY = 0;
        tmp_left = (int) service.getResources().getDimension(R.dimen.battery_text_left);
        if(!service.getResources().getBoolean(R.bool.battery_left_align)) {
            // If text is centered, set rectangle
            power.setRect(
                    (int) (2 * tmp_left + 640),
                    (int) (service.getResources().getDimension(R.dimen.battery_font_size))
            );
            tmp_left = -320;
        }
        power.setStart(
                tmp_left,
                (int) (service.getResources().getDimension(R.dimen.battery_text_top)-((float)service.getResources().getInteger(R.integer.font_ratio)/100)*service.getResources().getDimension(R.dimen.battery_font_size))
        );
        // Hide if disabled
        if(!service.getResources().getBoolean(R.bool.battery)){power.show=false;}

        // Show steps (today)
        SlptLinearLayout steps = new SlptLinearLayout();
        steps.add(new SlptTodayStepNumView());
        steps.setTextAttrForAll(
                service.getResources().getDimension(R.dimen.steps_font_size),
                service.getResources().getColor(R.color.steps_colour_slpt),
                timeTypeFace
        );
        // Position based on screen on
        steps.alignX = 2;
        steps.alignY = 0;
        tmp_left = (int) service.getResources().getDimension(R.dimen.steps_text_left);
        if(!service.getResources().getBoolean(R.bool.steps_left_align)) {
            // If text is centered, set rectangle
            steps.setRect(
                    (int) (2 * tmp_left + 640),
                    (int) (service.getResources().getDimension(R.dimen.steps_font_size))
            );
            tmp_left = -320;
        }
        steps.setStart(
                (int) tmp_left,
                (int) (service.getResources().getDimension(R.dimen.steps_text_top)-((float)service.getResources().getInteger(R.integer.font_ratio)/100)*service.getResources().getDimension(R.dimen.steps_font_size))
        );
        // Hide if disabled
        if(!service.getResources().getBoolean(R.bool.steps)){steps.show=false;}


        return Arrays.asList(new SlptViewComponent[]{steps, power });
    }
}