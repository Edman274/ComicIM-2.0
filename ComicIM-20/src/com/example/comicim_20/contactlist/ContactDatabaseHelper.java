package com.example.comicim_20.contactlist;

import com.example.comicim_20.Contact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class ContactDatabaseHelper extends SQLiteOpenHelper {
	public ContactDatabaseHelper(Context ctx) {
		super(ctx, "comicim.db", null, 1);
	}

	public void insert(Contact c) {
		ContentValues cv = new ContentValues();
		cv.put("phoneNumber", c.phoneNumber);
		
		this.getWritableDatabase().insert("contacts", "phoneNumber", cv);
	}
	
	public Cursor selectAll() {
		return this.getReadableDatabase().rawQuery("select * from restaurants order by name", null);
	}
	
	public Contact get(Cursor cursor) {
		return new Contact(cursor.getString(1));
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table contacts (_id int primary key autoincrement," +
	            "phoneNumber text);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}
