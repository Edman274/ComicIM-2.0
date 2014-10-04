package com.example.comicim_20.contactlist;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.comicim_20.*;

public final class ContactListAdapter extends ArrayAdapter<Contact> {
	public Context context;
	public List<Contact> model;
	
	public ContactListAdapter(Context ctx, List<Contact> model) {
		super(ctx, R.layout.contact_row, model);
		this.context = ctx;
		this.model = model;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ContactViewHolder holder = null;
		
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			view = inflater.inflate(R.layout.contact_row, null);
			holder = new ContactViewHolder(view);
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ContactViewHolder) convertView.getTag();
		}
		
		holder.populate(model.get(position));
		return view;
	}
}
