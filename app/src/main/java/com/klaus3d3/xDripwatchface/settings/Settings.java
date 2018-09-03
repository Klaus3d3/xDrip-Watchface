package com.klaus3d3.xDripwatchface.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.klaus3d3.xDripwatchface.Constants;
import com.klaus3d3.xDripwatchface.settings.APsettings;
import com.klaus3d3.xDripwatchface.R;

// BY GREATAPO

public class Settings extends FragmentActivity {


    public int currentLanguage = 0;
    private com.klaus3d3.xDripwatchface.settings.APsettings settings;
    // Languages
    public static String[] codes = {
            "English", "中文", "Czech", "Nederlands", "Français", "Deutsch", "Ελληνικά", "עברית", "Magyar", "Italiano", "日本語", "한국어", "Polski", "Português", "Русский", "Slovenčina", "Español", "Türkçe",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setup activity
        setContentView(R.layout.settings);

        // Load settings
        try {
            this.settings = new APsettings(Constants.PACKAGE_NAME, this.getApplicationContext().createPackageContext(Constants.PACKAGE_NAME, Context.CONTEXT_IGNORE_SECURITY));
        }catch(Exception e){
            Log.e("xDripwidget",e.toString());}

        // Save button
        TextView about_button = (TextView) findViewById(R.id.about);
        about_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Made by GreatApo & Klaus3d3", Toast.LENGTH_SHORT).show();
            }
        });

        // Save button
        TextView close_settings_button = (TextView) findViewById(R.id.close_settings);
        close_settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "Made by GreatApo", Toast.LENGTH_SHORT).show();
                change_watchface();
            }
        });





        // Language button
        TextView language_button = (TextView) findViewById(R.id.language);
        language_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateLanguage(v);
            }
        });
        this.currentLanguage = this.settings.get("lang", this.currentLanguage) % this.codes.length;
        // Show ui in new language
        ((TextView) this.findViewById(R.id.language)).setText(this.codes[this.currentLanguage]);
    }



    // Change to the next language
    private void rotateLanguage(View v) {
        this.currentLanguage = (this.currentLanguage + 1) % this.codes.length;
        // Show ui in new language
        ((TextView) v.findViewById(R.id.language)).setText(this.codes[this.currentLanguage]);
        // Save lang
        this.settings.set("lang", this.currentLanguage);
    }

    // Change watchface
    private void change_watchface() {
        this.sendBroadcast(new Intent("com.huami.intent.action.WATCHFACE_CONFIG_CHANGED"));

        this.setResult(-1);
        this.finish();
    }
}