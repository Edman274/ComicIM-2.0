package com.comicim.messageview;

import java.util.ArrayList;
import java.util.List;

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

public class MessageAdapter extends ArrayAdapter<List<Message>> {
	
	public Context context;
	public List<List<Message>> groupedMessages;
	
	public MessageAdapter(Context ctx, List<List<Message>> groupedMessages) {
		super(ctx, R.layout.message_row, groupedMessages);
		this.context = ctx;
		this.groupedMessages = groupedMessages;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		MessageRowHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			view = inflater.inflate(R.layout.message_row, null);
			holder = new MessageRowHolder(context, view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (MessageRowHolder)convertView.getTag();
		}
		holder.populate(groupedMessages.get(position));
		return view;
	}
	
}
