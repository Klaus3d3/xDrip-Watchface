package com.klaus3d3.xDripwatchface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.provider.Settings;
import android.util.Log;
import com.klaus3d3.xDripwatchface.CustomDataUpdater;

import com.github.marlonlom.utilities.timeago.TimeAgo;

import org.json.JSONException;
import org.json.JSONObject;

public class TimeTick extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        CustomDataUpdater.savetoSettings(context);

    }
}
