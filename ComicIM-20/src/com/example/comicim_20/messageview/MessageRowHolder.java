package com.example.comicim_20.messageview;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.comicim_20.Conversation;
import com.example.comicim_20.Message;
import com.example.comicim_20.R;
import com.example.comicim_20.scene.SceneView;

public class MessageRowHolder {
	public Context context;
	public ImageView background;
	public TextView speech;
	public SceneView comicFrame;
	
	public MessageRowHolder(Context context, View v) {
		background = (ImageView) v.findViewById(R.id.picBackground);
		speech = (TextView)v.findViewById(R.id.speech);
		comicFrame = new SceneView();
		this.context = context;
		
		
	}
	
	public void populate(Message message) {
		if (message.text.contains("think")) {
			comicFrame.newImage(this.context, message.text);
			background.setImageResource(R.drawable.dude_thinking);
		} else if (message.text.contains("happy") || message.text.contains("dance")) {
			background.setImageResource(R.drawable.dude_dancing);
		} else {
			background.setImageResource(R.drawable.dude);
		}		
		background.setImageDrawable(comicFrame.newImage(this.context, message.text));
		
		
		//speech.setText(message.text);
	}
	
}
