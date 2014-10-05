package com.comicim.contactlist;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.comicim.*;
import com.example.comicim_20.R;

public final class ConversatonListAdapter extends ArrayAdapter<Conversation> {
	public Context context;
	public List<Conversation> model;
	
	public ConversatonListAdapter(Context ctx, List<Conversation> model) {
		super(ctx, R.layout.contact_row, model);
		this.context = ctx;
		this.model = model;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ConversationViewHolder holder = null;
		
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			view = inflater.inflate(R.layout.contact_row, null);
			holder = new ConversationViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ConversationViewHolder) convertView.getTag();
		}
		
		holder.populate(model.get(position));
		return view;
	}
}
