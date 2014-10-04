package com.example.comicim_20.messageview;

import java.util.ArrayList;

import com.example.comicim_20.R;
import com.example.comicim_20.contactlist.ContactViewHolder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class MessageAdapter extends ArrayAdapter<String> {
	
	public Context context;
	public ArrayList<String> messageList;
	
	public MessageAdapter(Context ctx, ArrayList<String> messageList) {
		super(ctx, R.layout.message_row, messageList);
		this.context = ctx;
		this.messageList = messageList;
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
		holder.populate(messageList.get(position));
		return view;
	}
	
}
