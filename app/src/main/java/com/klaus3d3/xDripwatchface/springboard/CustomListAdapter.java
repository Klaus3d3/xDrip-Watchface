package com.klaus3d3.xDripwatchface.springboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.wearable.view.CircledImageView;


import com.huami.watch.common.widget.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.klaus3d3.xDripwatchface.R;

import java.util.List;


public class CustomListAdapter extends HmWearableListView.Adapter {
	private final List<MenuItems> items;
	private final LayoutInflater mInflater;

    private static final float NO_ALPHA = 1.0f, PARTIAL_ALPHA = 0.65f;
    private static final float NO_SCALE = 1.0f, SCALE = 0.75f;
    private static final float NO_X_TRANSLATION = 0f, X_TRANSLATION = 25f;

	public CustomListAdapter(Context context, List<MenuItems> items) {
		mInflater = LayoutInflater.from(context);
		this.items = items;
	}

	@Override
	public @NonNull HmWearableListView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		return new ItemViewHolder(mInflater.inflate(R.layout.list_item, null));
	}

	@Override
	public void onBindViewHolder(@NonNull HmWearableListView.ViewHolder viewHolder, int position) {
		//System.out.println("CustomListAdapter onBindViewHodler position: " + position);
		ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
		final MenuItems item = items.get(position);
		HmTextView textView = itemViewHolder.mItemTextView;
		textView.setText(item.title);
		CircledImageView mImageView = itemViewHolder.mCircledImageView;
		mImageView.setImageResource(item.iconRes);
		if (position == 1) {
            mImageView.animate().scaleX(NO_SCALE).scaleY(NO_SCALE).translationX(NO_X_TRANSLATION).alpha(NO_ALPHA).setDuration(0L);
            textView.animate().scaleX(NO_SCALE).scaleY(NO_SCALE).translationX(NO_X_TRANSLATION).alpha(NO_ALPHA).setDuration(0L);
            //mImageView.animate().alpha(PARTIAL_ALPHA).translationX(X_TRANSLATION).start();
            //textView.animate().alpha(PARTIAL_ALPHA).translationX(X_TRANSLATION).start();
        }

	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	private static class ItemViewHolder extends HmWearableListView.ViewHolder {
		private HmTextView mItemTextView;
		private CircledImageView mCircledImageView;

		public ItemViewHolder(View itemView) {
			super(itemView);
			mItemTextView = itemView.findViewById(R.id.text);
			mCircledImageView = itemView.findViewById(R.id.image);
		}
	}
}