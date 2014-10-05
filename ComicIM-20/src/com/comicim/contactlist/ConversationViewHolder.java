package com.comicim.contactlist;

import java.io.ByteArrayInputStream;

import com.comicim.Conversation;
import com.example.comicim_20.R;
import com.example.comicim_20.R.id;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public final class ConversationViewHolder {
	public ImageView photo;
	public TextView name;
	public TextView lastTime;
	public ImageView statusIndicator;
	public TextView messageCount;
	
	public ConversationViewHolder(View v) {
		photo = (ImageView) v.findViewById(R.id.contact_row_photo);
		name = (TextView) v.findViewById(R.id.contact_row_name);
		lastTime = (TextView) v.findViewById(R.id.contact_row_last_date);
		statusIndicator = (ImageView) v.findViewById(R.id.contact_row_status_indicator);
		messageCount = (TextView) v.findViewById(R.id.contact_row_message_count);
	}
	
	public void populate(Context ctx, Conversation c) {
		if (c.picture == null) {
			photo.setImageResource(R.drawable.ic_action_person);
		} else {
			photo.setImageBitmap(BitmapFactory.decodeByteArray(c.picture, 0, c.picture.length));
		}
		
		messageCount.setText(c.phoneNumber);
		name.setText(c.name);
	}
}
