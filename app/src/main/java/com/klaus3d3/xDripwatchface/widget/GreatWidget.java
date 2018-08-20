package com.klaus3d3.xDripwatchface.widget;

import android.app.Service;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.TextPaint;
import android.util.Base64;
import android.util.Log;


import com.ingenic.iwds.slpt.view.sport.SlptPowerNumView;
import com.ingenic.iwds.slpt.view.sport.SlptTodayStepNumView;
import com.klaus3d3.xDripwatchface.CustomDataUpdater;
import com.klaus3d3.xDripwatchface.data.Alarm;
import com.klaus3d3.xDripwatchface.data.Battery;
import com.klaus3d3.xDripwatchface.data.CustomData;
import com.klaus3d3.xDripwatchface.data.HeartRate;
import com.klaus3d3.xDripwatchface.data.Steps;
import com.klaus3d3.xDripwatchface.data.Xdrip;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.huami.watch.watchface.util.Util;
import com.ingenic.iwds.slpt.view.core.SlptLinearLayout;
import com.ingenic.iwds.slpt.view.core.SlptNumView;
import com.ingenic.iwds.slpt.view.core.SlptPictureView;
import com.ingenic.iwds.slpt.view.core.SlptViewComponent;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.klaus3d3.xDripwatchface.data.DataType;
import com.klaus3d3.xDripwatchface.data.Time;
import com.klaus3d3.xDripwatchface.resource.ResourceManager;
import com.klaus3d3.xDripwatchface.R;
import com.ingenic.iwds.slpt.view.digital.SlptHourHView;
import com.ingenic.iwds.slpt.view.digital.SlptHourLView;
import com.ingenic.iwds.slpt.view.digital.SlptMinuteHView;
import com.ingenic.iwds.slpt.view.digital.SlptMinuteLView;
import com.ingenic.iwds.slpt.view.utils.SimpleFile;


public class GreatWidget extends AbstractWidget {

    private HeartRate HR;

    private Alarm alarmData;
    private Xdrip xdripData;
    private Battery batteryData;
    private Steps stepsData;

    private TextPaint ampmPaint;
    private Paint PhoneBatteryPaint;
    private Drawable background;
    private TextPaint alarmPaint;
    private TextPaint xdripsgvPaint;
    private TextPaint xdripDeltaPaint;
    private TextPaint xdripDatePaint;
    private Paint xdripgraphpaint;
    private TextPaint hourFont;


    private Boolean alarmBool;
    private Boolean alarmAlignLeftBool;

    private Boolean ampmAlignLeftBool;
    private Boolean xdripBool;
    private Boolean xdripAlignLeftBool;
    private Boolean phonebatteryLeftBool;
    private float leftHour;
    private float topHour;
    private boolean no_0_on_hour_first_digit;


    private Service mService;


    private float alarmTop;
    private float alarmLeft;
    private float xdripTop;
    private float xdripLeft;
    private String[] digitalNums = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private String[] digitalNumsNo0 = {"", "1", "2", "3", "4", "5", "6", "7", "8", "9"};//no 0 on first digit
    private Bitmap graph;
    private Paint batteryPaint;
    private Paint stepsPaint;

    private boolean batteryBool;
    private boolean stepsBool;

    private boolean batteryAlignLeftBool;
    private boolean stepsAlignLeftBool;

    private float batteryTextLeft;
    private float batteryTextTop;
    private float stepsTextLeft;
    private float stepsTextTop;




    @Override
    public void init(Service service){
        Log.e("GreatWidget", "init");
        // This service
        this.mService = service;





        // Get xdrip
        this.xdripData = getXdrip();



        this.leftHour = service.getResources().getDimension(R.dimen.hours_left);
        this.topHour = service.getResources().getDimension(R.dimen.hours_top);
        this.hourFont = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.hourFont.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MONO_SPACE));
        this.hourFont.setTextSize(service.getResources().getDimension(R.dimen.hours_font_size));
        this.hourFont.setColor(service.getResources().getColor(R.color.hour_colour));
        this.hourFont.setTextAlign(Paint.Align.CENTER);





        this.alarmLeft = service.getResources().getDimension(R.dimen.alarm_left);
        this.alarmTop = service.getResources().getDimension(R.dimen.alarm_top);
        this.xdripLeft = service.getResources().getDimension(R.dimen.xdrip_left);
        this.xdripTop = service.getResources().getDimension(R.dimen.xdrip_top);


        this.background = service.getResources().getDrawable(R.drawable.background_red);

        this.background.setBounds(0, 220, 320, 300);

        this.ampmAlignLeftBool = service.getResources().getBoolean(R.bool.ampm_left_align);
        this.phonebatteryLeftBool=service.getResources().getBoolean(R.bool.battery_left_align);
        this.alarmBool = service.getResources().getBoolean(R.bool.alarm);
        this.alarmAlignLeftBool = service.getResources().getBoolean(R.bool.alarm_left_align);
        this.xdripBool = service.getResources().getBoolean(R.bool.xdrip);
        this.xdripAlignLeftBool = service.getResources().getBoolean(R.bool.xdrip_left_align);



        this.ampmPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.ampmPaint.setColor(service.getResources().getColor(R.color.ampm_colour));
        this.ampmPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE));
        this.ampmPaint.setTextSize(service.getResources().getDimension(R.dimen.ampm_font_size));
        this.ampmPaint.setTextAlign( (this.ampmAlignLeftBool) ? Paint.Align.LEFT : Paint.Align.CENTER );

        this.alarmPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.alarmPaint.setColor(service.getResources().getColor(R.color.alarm_colour));
        this.alarmPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE));
        this.alarmPaint.setTextSize(service.getResources().getDimension(R.dimen.alarm_font_size));
        this.alarmPaint.setTextAlign( (this.alarmAlignLeftBool) ? Paint.Align.LEFT : Paint.Align.CENTER );

        this.xdripsgvPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.xdripsgvPaint.setColor(Color.parseColor(xdripData.color));
        this.xdripsgvPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MONO_SPACE));
        this.xdripsgvPaint.setTextSize(service.getResources().getDimension(R.dimen.xdrip_font_size));
        this.xdripsgvPaint.setTextAlign((this.xdripAlignLeftBool) ? Paint.Align.LEFT : Paint.Align.CENTER);

        this.xdripDeltaPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.xdripDeltaPaint.setColor(Color.parseColor(xdripData.color));
        this.xdripDeltaPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE));
        this.xdripDeltaPaint.setTextSize(service.getResources().getDimension(R.dimen.xdripdelta_font_size));
        this.xdripDeltaPaint.setTextAlign((this.xdripAlignLeftBool) ? Paint.Align.LEFT : Paint.Align.CENTER);

        this.xdripDatePaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        this.xdripDatePaint.setColor(service.getResources().getColor(R.color.xdrip_colour));
        this.xdripDatePaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE));
        this.xdripDatePaint.setTextSize(service.getResources().getDimension(R.dimen.xdripdate_font_size));
        this.xdripDatePaint.setTextAlign(Paint.Align.CENTER);
        // Widgets text colors
        this.PhoneBatteryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.PhoneBatteryPaint.setTypeface(ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MONO_SPACE));
        this.PhoneBatteryPaint.setTextSize(service.getResources().getDimension(R.dimen.steps_font_size));
        this.PhoneBatteryPaint.setColor(service.getResources().getColor(R.color.battery_colour));
        this.PhoneBatteryPaint.setTextAlign( (this.phonebatteryLeftBool) ? Paint.Align.LEFT : Paint.Align.CENTER );
        this.no_0_on_hour_first_digit = service.getResources().getBoolean(R.bool.no_0_on_hour_first_digit);

        this.batteryBool = service.getResources().getBoolean(R.bool.battery);
        this.stepsBool = service.getResources().getBoolean(R.bool.steps);

        this.batteryAlignLeftBool = service.getResources().getBoolean(R.bool.battery_left_align);
        this.stepsAlignLeftBool = service.getResources().getBoolean(R.bool.steps_left_align);
        this.batteryTextLeft = service.getResources().getDimension(R.dimen.battery_text_left);
        this.batteryTextTop = service.getResources().getDimension(R.dimen.battery_text_top);
        this.stepsTextLeft = service.getResources().getDimension(R.dimen.steps_text_left);
        this.stepsTextTop = service.getResources().getDimension(R.dimen.steps_text_top);

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
    }




    public byte[] BitmaptoByte(Bitmap bitmap) {
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* Ignored for PNGs */, blob);
        byte[] bitmapdata = blob.toByteArray();

        return bitmapdata;
    }

    @Override
    public void draw(Canvas canvas, float width, float height, float centerX, float centerY, int minutes, int hours) {


        // Draw Alarm, if enabled
        if(this.alarmBool) {
            canvas.drawText(this.alarmData.alarm, alarmLeft, alarmTop, alarmPaint);
        }

            // Draw Phonebattery
            canvas.drawText(xdripData.phonebattery+"%", mService.getResources().getDimension(R.dimen.phonebattery_text_left), mService.getResources().getDimension(R.dimen.phonebattery_text_top), PhoneBatteryPaint);


        // Draw Xdrip, if enabled
        if (this.xdripBool) {
            this.xdripsgvPaint.setColor(Color.parseColor(xdripData.color));
            this.xdripDeltaPaint.setColor(Color.parseColor(xdripData.color));
            this.xdripDatePaint.setColor(Color.parseColor(xdripData.color));
            if(xdripData.color.equals("WHITE"))this.background.draw(canvas);

            if(xdripData.firstdata && xdripData.watchfacegraph_bool)canvas.drawBitmap(xdripData.sgv_graph, mService.getResources().getDimension(R.dimen.xdripgraph_left), mService.getResources().getDimension(R.dimen.xdripgraph_top), xdripgraphpaint);
            canvas.drawText(xdripData.sgv, xdripLeft, xdripTop, xdripsgvPaint);
            canvas.drawText(xdripData.delta, mService.getResources().getDimension(R.dimen.xdripdelta_left), mService.getResources().getDimension(R.dimen.xdripdelta_top), xdripDeltaPaint);
            canvas.drawText(TimeAgo.using(xdripData.timestamp), mService.getResources().getDimension(R.dimen.xdripdate_left), mService.getResources().getDimension(R.dimen.xdripdate_top), xdripDatePaint);
            canvas.drawText(xdripData.strike, xdripLeft, xdripTop, xdripsgvPaint);

        }
        canvas.drawText( (this.no_0_on_hour_first_digit)?hours+"":Util.formatTime(hours)+Util.formatTime(minutes), this.leftHour, this.topHour, this.hourFont);

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
    public List<DataType> getDataTypes() {
        // For many refreshes
        //return Arrays.asList(DataType.BATTERY, DataType.STEPS, DataType.DISTANCE, DataType.TOTAL_DISTANCE, DataType.TIME,  DataType.CALORIES,  DataType.DATE,  DataType.HEART_RATE,  DataType.FLOOR, DataType.WEATHER);
        return Arrays.asList(DataType.TIME,  DataType.XDRIP, DataType.HEART_RATE, DataType.BATTERY, DataType.STEPS);
    }

    @Override
    public void onDataUpdate(DataType type, Object value) {
        //Log.w("DinoDevs-GreatFit", type.toString()+" => "+value.toString() );


        // On each Data updated
        switch (type) {

            case BATTERY:
                this.batteryData = (Battery) value;
                break;
            case TIME:
                break;
            case STEPS:
                this.stepsData = (Steps) value;
                break;
            case HEART_RATE:
                this.HR = (HeartRate) value;
                break;
            case XDRIP:
                // Update Xdrip
                this.xdripData = (Xdrip) value;
                this.graph=xdripData.sgv_graph;
               // Log.w("DinoDevs-GreatFit", type.toString()+" => "+value.toString() );
                break;

        }


    }


    public Time getSlptTime() {
        Calendar now = Calendar.getInstance();
        int periode = (now.get(Calendar.HOUR_OF_DAY) <= 12)?0:1;
        return new Time(periode);
    }





    public Xdrip getXdrip(){
        String str = Settings.System.getString(this.mService.getContentResolver(), "xdrip");

        return new Xdrip(str);
    }


    @Override
    public List<SlptViewComponent> buildSlptViewComponent(Service service) {
        // Variables
        // This service
        this.mService = service;



        int tmp_left;
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



        // Get next alarm


        // Get xdrip
        this.xdripData = getXdrip();

        SlptPictureView background = new SlptPictureView();
        background.setImagePicture(SimpleFile.readFileFromAssets(service, "background_red.png"));
        background.setStart((int) 0,  (int)220);
        if(xdripData.color.equals("WHITE")){background.show=true;}else background.show=false;



        // Show Phonebattery
        SlptLinearLayout Phonebattery = new SlptLinearLayout();
        SlptPictureView text = new SlptPictureView();
        text.setStringPicture(xdripData.phonebattery+"%");
        Phonebattery.add(text);
        Phonebattery.setTextAttrForAll(
                mService.getResources().getDimension(R.dimen.battery_font_size),
                mService.getResources().getColor(R.color.battery_colour_slpt),
                timeTypeFace
        );
        // Position based on screen on
        Phonebattery.alignX = 2;
        Phonebattery.alignY = 0;
        tmp_left = (int) mService.getResources().getDimension(R.dimen.phonebattery_text_left);
        if(!mService.getResources().getBoolean(R.bool.battery_left_align)) {
            // If text is centered, set rectangle
            Phonebattery.setRect(
                    (int) (2 * tmp_left + 640),
                    (int) (mService.getResources().getDimension(R.dimen.battery_font_size))
            );
            tmp_left = -320;
        }
        Phonebattery.setStart(
                tmp_left,
                (int) (mService.getResources().getDimension(R.dimen.phonebattery_text_top)-((float)mService.getResources().getInteger(R.integer.font_ratio)/100)*mService.getResources().getDimension(R.dimen.battery_font_size))
        );
        // Hide if disabled
        if(!mService.getResources().getBoolean(R.bool.battery)){Phonebattery.show=false;}




    // Draw XdripGraph

    SlptPictureView xdripGraphLayout = new SlptPictureView();
    if (xdripData.firstdata) {
        xdripGraphLayout.setImagePicture(BitmaptoByte(xdripData.sgv_graph));
        // Position based on screen on
        xdripGraphLayout.setStart((int) service.getResources().getDimension(R.dimen.xdripgraph_left), (int) service.getResources().getDimension(R.dimen.xdripgraph_top));

    }
    if (!xdripData.firstdata || !xdripData.watchfacegraph_bool) xdripGraphLayout.show = false;
    // Draw hours

    SlptLinearLayout hourLayout = new SlptLinearLayout();
    if (service.getResources().getBoolean(R.bool.no_0_on_hour_first_digit)) {// No 0 on first digit
        SlptViewComponent firstDigit = new SlptHourHView();
        ((SlptNumView) firstDigit).setStringPictureArray(digitalNumsNo0);
        hourLayout.add(firstDigit);
        SlptViewComponent secondDigit = new SlptHourLView();
        ((SlptNumView) secondDigit).setStringPictureArray(digitalNums);
        hourLayout.add(secondDigit);

    } else {
        hourLayout.add(new SlptHourHView());
        hourLayout.add(new SlptHourLView());
        hourLayout.add(new SlptMinuteHView());
        hourLayout.add(new SlptMinuteLView());
        hourLayout.setStringPictureArrayForAll(this.digitalNums);
    }
    hourLayout.setTextAttrForAll(
            service.getResources().getDimension(R.dimen.hours_font_size),
            service.getResources().getColor(R.color.hour_colour_slpt),
            timeTypeFace
    );
    // Position based on screen on
    hourLayout.alignX = 2;
    hourLayout.alignY = 0;
    hourLayout.setRect(
            (int) (2 * service.getResources().getDimension(R.dimen.hours_left) + 640),
            (int) (service.getResources().getDimension(R.dimen.hours_font_size))
    );
    hourLayout.setStart(
            -320,
            (int) (service.getResources().getDimension(R.dimen.hours_top) - ((float) service.getResources().getInteger(R.integer.font_ratio) / 100) * service.getResources().getDimension(R.dimen.hours_font_size))
    );

        // Draw Xdrip SGV
        SlptLinearLayout xdripSGVLayout = new SlptLinearLayout();
        SlptPictureView xdripSGVStr = new SlptPictureView();
        xdripSGVStr.setStringPicture( xdripData.sgv );
        xdripSGVLayout.add(xdripSGVStr);
        xdripSGVLayout.setTextAttrForAll(
                service.getResources().getDimension(R.dimen.xdrip_font_size),
                Color.parseColor(xdripData.color),
                ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MONO_SPACE)
        );
        // Position based on screen on
        xdripSGVLayout.alignX = 2;
        xdripSGVLayout.alignY = 0;
        tmp_left = (int) service.getResources().getDimension(R.dimen.xdrip_left);
        if(!service.getResources().getBoolean(R.bool.xdrip_left_align)) {
            // If text is centered, set rectangle
            xdripSGVLayout.setRect(
                    (int) (2 * tmp_left + 640),
                    (int) (service.getResources().getDimension(R.dimen.xdrip_font_size))
            );
            tmp_left = -320;
        }
        xdripSGVLayout.setStart(
                (int) tmp_left,
                (int) (service.getResources().getDimension(R.dimen.xdrip_top)-((float)service.getResources().getInteger(R.integer.font_ratio)/100)*service.getResources().getDimension(R.dimen.xdrip_font_size))
        );
        // Hide if disabled
        if(!service.getResources().getBoolean(R.bool.xdrip)){xdripSGVLayout.show=false;}

        // Draw Xdrip Strike
        SlptLinearLayout xdripStrikeSGVLayout = new SlptLinearLayout();
        SlptPictureView xdripStrikeSGVStr = new SlptPictureView();
        xdripStrikeSGVStr.setStringPicture( xdripData.strike );
        xdripStrikeSGVLayout.add(xdripStrikeSGVStr);
        xdripStrikeSGVLayout.setTextAttrForAll(
                service.getResources().getDimension(R.dimen.xdrip_font_size),
                Color.parseColor(xdripData.color),
                ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MONO_SPACE)
        );
        // Position based on screen on
        xdripStrikeSGVLayout.alignX = 2;
        xdripStrikeSGVLayout.alignY = 0;
        tmp_left = (int) service.getResources().getDimension(R.dimen.xdrip_left);
        if(!service.getResources().getBoolean(R.bool.xdrip_left_align)) {
            // If text is centered, set rectangle
            xdripStrikeSGVLayout.setRect(
                    (int) (2 * tmp_left + 640),
                    (int) (service.getResources().getDimension(R.dimen.xdrip_font_size))
            );
            tmp_left = -320;
        }
        xdripStrikeSGVLayout.setStart(
                (int) tmp_left,
                (int) (service.getResources().getDimension(R.dimen.xdrip_top)-((float)service.getResources().getInteger(R.integer.font_ratio)/100)*service.getResources().getDimension(R.dimen.xdrip_font_size))
        );



        // Draw Xdrip Delta
        SlptLinearLayout xdripDeltaLayout = new SlptLinearLayout();
        SlptPictureView xdripDeltaStr = new SlptPictureView();
        xdripDeltaStr.setStringPicture( xdripData.delta );
        xdripDeltaLayout.add(xdripDeltaStr);
        xdripDeltaLayout.setTextAttrForAll(
                service.getResources().getDimension(R.dimen.xdripdelta_font_size),
                Color.parseColor(xdripData.color),
                ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE)
        );
        // Position based on screen on
        xdripDeltaLayout.alignX = 2;
        xdripDeltaLayout.alignY = 0;
        tmp_left = (int) service.getResources().getDimension(R.dimen.xdripdelta_left);
        if(!service.getResources().getBoolean(R.bool.xdrip_left_align)) {
            // If text is centered, set rectangle
            xdripDeltaLayout.setRect(
                    (int) (2 * tmp_left + 640),
                    (int) (service.getResources().getDimension(R.dimen.xdripdelta_font_size))
            );
            tmp_left = -320;
        }
        xdripDeltaLayout.setStart(
                (int) tmp_left,
                (int) (service.getResources().getDimension(R.dimen.xdripdelta_top)-((float)service.getResources().getInteger(R.integer.font_ratio)/100)*service.getResources().getDimension(R.dimen.xdripdelta_font_size))
        );
        // Hide if disabled
        if(!service.getResources().getBoolean(R.bool.xdrip)){xdripDeltaLayout.show=false;}



        // Draw Xdrip Date
        SlptLinearLayout xdripDateLayout = new SlptLinearLayout();
        SlptPictureView xdripDateStr = new SlptPictureView();
        xdripDateStr.setStringPicture( xdripData.timeago );
        xdripDateLayout.add(xdripDateStr);
        xdripDateLayout.setTextAttrForAll(
                service.getResources().getDimension(R.dimen.xdripdate_font_size),
                Color.parseColor(xdripData.color),
                ResourceManager.getTypeFace(service.getResources(), ResourceManager.Font.MULTI_SPACE)
        );
        // Position based on screen on
        xdripDateLayout.alignX = 2;
        xdripDateLayout.alignY = 0;
        tmp_left = (int) service.getResources().getDimension(R.dimen.xdripdate_left);
        if(!service.getResources().getBoolean(R.bool.xdrip_date_left_align)) {
            // If text is centered, set rectangle
            xdripDateLayout.setRect(
                    (int) (2 * tmp_left + 640),
                    (int) (service.getResources().getDimension(R.dimen.xdripdate_font_size))
            );
            tmp_left = -320;
        }
        xdripDateLayout.setStart(
                (int) tmp_left,
                (int) (service.getResources().getDimension(R.dimen.xdripdate_top)-((float)service.getResources().getInteger(R.integer.font_ratio)/100)*service.getResources().getDimension(R.dimen.xdripdate_font_size))
        );
        // Hide if disabled
        if(!service.getResources().getBoolean(R.bool.xdrip)){xdripDateLayout.show=false;}

        return Arrays.asList(new SlptViewComponent[]{ background,xdripGraphLayout,hourLayout,xdripSGVLayout,xdripDeltaLayout,xdripDateLayout,Phonebattery,xdripStrikeSGVLayout,power,steps});
    }
}
