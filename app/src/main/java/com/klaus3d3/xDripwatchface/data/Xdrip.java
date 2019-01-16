package com.klaus3d3.xDripwatchface.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.klaus3d3.xDripwatchface.R;
import com.klaus3d3.xDripwatchface.widget.GreatWidget;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import com.klaus3d3.xDripwatchface.settings.APsettings;
import com.klaus3d3.xDripwatchface.Constants;
import android.util.Log;

public class Xdrip {

    public String JSONstr="";
    public String strike="";
    public String color="WHITE";
    public String sgv="--";
    public String delta="--";
    public String timeago="--";
    public String phonebattery="--";
    public String sgv_graph="false";
    public Long timestamp=Long.valueOf(1);
    public Boolean badvalue=false;
    public Boolean ishigh=false;
    public Boolean islow=false;
    public Boolean isstale=false;
    public Boolean firstdata=false;


    public Xdrip(String parmStr1) {
        this.JSONstr = parmStr1;



        if(parmStr1!=null && !parmStr1.equals("")){

            try {
                // Extract data from JSON
                JSONObject json_data = new JSONObject(parmStr1);
                this.sgv = json_data.getString("sgv");
                this.delta = json_data.getString("delta");
                this.timestamp=Long.valueOf(json_data.getString("date"));
                this.timeago = TimeAgo.using(Long.valueOf(json_data.getString("date")));;
                this.sgv_graph=json_data.getString("WFGraph");
                this.phonebattery=json_data.getString("phone_battery");
                this.ishigh=Boolean.valueOf(json_data.getString("ishigh"));
                this.islow=Boolean.valueOf(json_data.getString("islow"));
                this.isstale=Boolean.valueOf(json_data.getString("isstale"));
                if (!this.isstale&&!this.islow&&!this.ishigh) this.color="BLACK";
                else this.color="WHITE";

                if(System.currentTimeMillis()>this.timestamp+10*60*1000)
                this.strike=new String(new char[this.sgv.length()]).replace("\0", "─");
                else this.strike="";
                this.firstdata=true;
            }catch (Exception e) {
                // Nothing
            }
        }

    }

    public static String Epoch2DateString(long epoch, String formatString) {
        java.util.Date updatedate = new Date(epoch);
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.format(updatedate);
    }

    public String toString() {
        // Default onDataUpdate value
        return String.format("[Xdrip data info] Sting 1:%s", this.JSONstr);
    }



}