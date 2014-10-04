package com.example.comicim_20.contactlist;

import com.example.comicim_20.Contact;
import com.example.comicim_20.R;
import com.example.comicim_20.R.id;

import android.view.View;
import android.widget.TextView;

public final class ContactViewHolder {
	public TextView phoneNumber;
	
	public ContactViewHolder(View v) {
		phoneNumber = (TextView) v.findViewById(R.id.contact_row_phone);
	}
	
	public void populate(Contact c) {
		phoneNumber.setText(c.phoneNumber);
	}
}
