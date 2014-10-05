package com.comicim.messageview;

import java.util.ArrayList;

import com.comicim.Conversation;
import com.comicim.Message;
import com.comicim.contactlist.ConversationViewHolder;
import com.example.comicim_20.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class MessageAdapter extends ArrayAdapter<Message> {
	
	public Context context;
	public Conversation conversation;
	
	public MessageAdapter(Context ctx, Conversation conversation) {
		super(ctx, R.layout.message_row, conversation.messages);
		this.context = ctx;
		this.conversation = conversation;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		MessageRowHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			view = inflater.inflate(R.layout.message_row, null);
			holder = new MessageRowHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (MessageRowHolder)convertView.getTag();
		}
		holder.populate(conversation.messages.get(position));
		return view;
	}
	
}
