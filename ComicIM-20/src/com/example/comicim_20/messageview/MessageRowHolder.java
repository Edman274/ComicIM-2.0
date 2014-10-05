package com.example.comicim_20.messageview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.comicim_20.Conversation;
import com.example.comicim_20.Message;
import com.example.comicim_20.R;

public class MessageRowHolder {

	public ImageView background;
	public TextView speech;
	
	public MessageRowHolder(View v) {
		background = (ImageView) v.findViewById(R.id.picBackground);
		speech = (TextView)v.findViewById(R.id.speech);
	}
	
	public void populate(Message message) {
		if (message.text.contains("think")) {
			background.setImageResource(R.drawable.dude_thinking);
		} else if (message.text.contains("happy") || message.text.contains("dance")) {
			background.setImageResource(R.drawable.dude_dancing);
		} else {
			background.setImageResource(R.drawable.dude);
		}		
		speech.setText(message.text);
	}
	
}
