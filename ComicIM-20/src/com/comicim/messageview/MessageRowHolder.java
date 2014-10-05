package com.comicim.messageview;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.comicim.Conversation;
import com.comicim.Message;
import com.example.comicim_20.R;
import com.comicim.scene.SceneView;

public class MessageRowHolder {
	public Context context;
	public SceneView sceneView;
	
	public MessageRowHolder(Context context, View v) {
		this.context = context;
		sceneView = (SceneView) v.findViewById(R.id.picBackground);
	}
	
	public void populate(List<Message> messages) {
		sceneView.setMessages(messages);
	}
	
}
