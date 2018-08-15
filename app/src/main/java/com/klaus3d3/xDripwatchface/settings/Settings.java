package com.klaus3d3.xDripwatchface.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

<<<<<<< HEAD:app/src/main/java/com/klaus3d3/xDripwatchface/settings/Settings.java
import com.klaus3d3.xDripwatchface.widget.MainClock;
import com.klaus3d3.xDripwatchface.R;
=======
import com.dinodevs.greatfitwatchface.R;
>>>>>>> 94587dd10f0ec9e982cf95285090c7b5c382bfed:app/src/main/java/com/dinodevs/greatfitwatchface/settings/Settings.java

import java.util.ArrayList;
import java.util.List;

public class Settings extends FragmentActivity {

<<<<<<< HEAD:app/src/main/java/com/klaus3d3/xDripwatchface/settings/Settings.java
    public static String[] color = {"#ff0000", "#00ffff","#00ff00","#ff00ff","#ffffff","#ffff00"};
    public int currentColor = 3;
    public int currentLanguage = 0;
    private com.klaus3d3.xDripwatchface.settings.APsettings settings;
    // Languages
    public static String[] codes = {
            "English", "中文", "Czech", "Français", "Deutsch", "Ελληνικά", "עברית", "Magyar", "Italiano", "日本語", "Polski", "Português", "Русский", "Slovenčina", "Español"//, "Türkçe",
    };
=======
    /*
        Activity to provide a settings list for choosing vibration and sub-settings
        Made by KieronQuinn for AmazfitStepNotify
        Modified by GreatApo
     */
>>>>>>> 94587dd10f0ec9e982cf95285090c7b5c382bfed:app/src/main/java/com/dinodevs/greatfitwatchface/settings/Settings.java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView root = new RecyclerView(this);
        final SharedPreferences sharedPreferences = getSharedPreferences(getPackageName()+"_settings", Context.MODE_PRIVATE);

        //Add header to a list of settings
        List<BaseSetting> settings = new ArrayList<>();

<<<<<<< HEAD:app/src/main/java/com/klaus3d3/xDripwatchface/settings/Settings.java
        // Save button
        TextView about_button = (TextView) findViewById(R.id.about);
        about_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Made by GreatApo& Klaus3d3", Toast.LENGTH_SHORT).show();
            }
        });
=======
        //Add IconSettings for each sub-setting. They contain an icon, title and subtitle, as well as a click action to launch the sub-setting's activity
        settings.add(new HeaderSetting(getString(R.string.settings)));
>>>>>>> 94587dd10f0ec9e982cf95285090c7b5c382bfed:app/src/main/java/com/dinodevs/greatfitwatchface/settings/Settings.java

        settings.add(new IconSetting(getDrawable(R.drawable.palette), getString(R.string.main_color), getString(R.string.main_color_c), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, ColorActivity.class));
            }
        }, null));

        settings.add(new IconSetting(getDrawable(R.drawable.widgets), getString(R.string.elements), getString(R.string.elements_c), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, WidgetsActivity.class));
            }
        }, null));

        settings.add(new IconSetting(getDrawable(R.drawable.language), getString(R.string.language), getString(R.string.language_c), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, LanguageActivity.class));
            }
        }, null));

        //Add save button
        settings.add(new ButtonSetting(getString(R.string.save), getDrawable(R.drawable.green_button), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do settings stuff here

                // Set watchface
                //Intent intent = new Intent("com.dinodevs.greatfitwatchface.GreatFitSlpt");
                //finish();
                //Settings.this.sendBroadcast(new Intent("com.dinodevs.greatfitwatchface.GreatFitSlpt"));
                Settings.this.sendBroadcast(new Intent("com.huami.intent.action.WATCHFACE_CONFIG_CHANGED"));

                Settings.this.setResult(-1);
                Settings.this.finish();
            }
        }));

        //Add reset button
        settings.add(new ButtonSetting(getString(R.string.reset), getDrawable(R.drawable.grey_button), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().clear().apply();
                Toast.makeText(view.getContext(), "Settings reset", Toast.LENGTH_SHORT).show();
                Settings.this.sendBroadcast(new Intent("com.huami.intent.action.WATCHFACE_CONFIG_CHANGED"));
                Settings.this.setResult(-1);
                Settings.this.finish();
            }
        }));

        //Setup layout
        root.setBackgroundResource(R.drawable.settings_background);
        root.setLayoutManager(new LinearLayoutManager(this));
        root.setAdapter(new Adapter(this, settings));
        root.setPadding((int) getResources().getDimension(R.dimen.padding_round_small), 0, (int) getResources().getDimension(R.dimen.padding_round_small), (int) getResources().getDimension(R.dimen.padding_round_large));
        root.setClipToPadding(false);
        setContentView(root);
    }
}
