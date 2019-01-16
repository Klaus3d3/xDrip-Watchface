package com.klaus3d3.xDripwatchface.springboard;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.wearable.view.WearableListView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.klaus3d3.xDripwatchface.R;
import com.huami.watch.common.widget.*;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends Activity implements HmWearableListView.ClickListener{
    private VerticalViewPager viewGroup;
    private int snooze_time;
    private int default_snooze=0;
    private String[] mItems = {"10 Minutes", "20 Minutes", "30 Minutes", "45 Minutes", "1 Hours", "2 Hours", "3 Hours", "4 Hours", "6 Hours", "8 Hours"};
    private int[] mImages = {R.drawable.snooze_white_54x54, R.drawable.snooze_white_54x54,
            R.drawable.snooze_white_54x54, R.drawable.snooze_white_54x54,R.drawable.snooze_white_54x54, R.drawable.snooze_white_54x54
            , R.drawable.snooze_white_54x54, R.drawable.snooze_white_54x54, R.drawable.snooze_white_54x54, R.drawable.snooze_white_54x54};
    private HmTextView mHeader;
    HmWearableListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        listview=findViewById(R.id.WearListeview);
        mHeader = findViewById(R.id.header);
        default_snooze=getIntent().getIntExtra("default_snooze",30);
        loadAdapter("Select Snooze time");

    }

    private void loadAdapter(String s) {
        mHeader.setText(s);
        List<MenuItems> items = new ArrayList<>();
        for (int i=0; i<mItems.length; i++){
            items.add(new MenuItems(mImages[i], mItems[i]));
        }

        CustomListAdapter mAdapter = new CustomListAdapter(this, items);

        listview.setAdapter(mAdapter);
        listview.addOnScrollListener(mOnScrollListener);
        listview.setClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onClick(HmWearableListView.ViewHolder viewHolder) {

        switch (viewHolder.getAdapterPosition()) {
            case 0:
                snooze_time=10;
                finish();
                break;
            case 1:
                snooze_time=20;
                finish();
                break;
            case 2:
                snooze_time=30;
                finish();
                break;
            case 3:
                snooze_time=45;
                finish();
                break;
            case 4:
                snooze_time=60;
                finish();
                break;
            case 5:
                snooze_time=120;
                finish();
                break;
            case 6:
                snooze_time=180;
                finish();
                break;
            case 7:
                snooze_time=240;
                finish();
                break;
            case 8:
                snooze_time=360;
                finish();
                break;
            case 9:
                snooze_time=480;
                finish();
                break;

            default:
                Toast.makeText(this,
                        String.format("You selected item #%s",
                                viewHolder.getAdapterPosition()),
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    public void onTopEmptyRegionClick() {
        //Prevent NullPointerException
        Toast.makeText(this,
                "Top empty area tapped", Toast.LENGTH_SHORT).show();
    }

    // The following code ensures that the title scrolls as the user scrolls up
    // or down the list
    private HmWearableListView.OnScrollListener mOnScrollListener =
            new HmWearableListView.OnScrollListener() {
                @Override
                public void onAbsoluteScrollChange(int i) {
                    // Only scroll the title up from its original base position
                    // and not down.
                    if (i > 0) {
                        mHeader.setY(-i);
                    }
                }

                @Override
                public void onScroll(int i) {

                }

                @Override
                public void onScrollStateChanged(int i) {
                    // Placeholder
                }

                @Override
                public void onCentralPositionChanged(int i) {

                }
            };

    @Override
    public void finish() {

        if (snooze_time==0){snooze_time=default_snooze;}
        Intent intent1 = new Intent("com.klaus3d3.xDripwatchface.snooze_alarm_intent");
        intent1.putExtra("snooze_time",snooze_time);
        sendBroadcast(intent1);
        super.finish();
    }
}
