package com.example.comicim_20.contactlist;

import java.util.ArrayList;
import java.util.List;

import com.example.comicim_20.Conversation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class ContactDatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = ContactDatabaseHelper.class.getPackage().getName();
	
    private static final String TABLE_CONVERSATIONS = "conversations";
    private static final String TABLE_MESSAGES = "messages";
    
    private static final String CREATE_TABLE_CONTACTS = 
    		"create table " + TABLE_CONVERSATIONS + "("
    		+ "id integer primary key, "
    		+ "phone_number text"
    		+ ")";
    private static final String CREATE_TABLE_MESSAGES = 
    		"create table " + TABLE_MESSAGES + "("
    		+ "id integer primary key, "
    		+ "contact_id integer, "
    		+ "time_sent integer, "
    		+ "time_received integer, "
    		+ "message_text text"
    		+ ")";
    
	public ContactDatabaseHelper(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_CONTACTS);
		db.execSQL(CREATE_TABLE_MESSAGES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONVERSATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
 
        // create new tables
        onCreate(db);
	}
	
	public Conversation newContact(String phoneNumber) {
		ContentValues cv = new ContentValues();
		cv.put("phone_number", phoneNumber);
		
		long id = this.getWritableDatabase().insert(TABLE_CONVERSATIONS, null, cv);
		return new Conversation(id, phoneNumber);
	}
	
	public Cursor selectContacts() {
		SQLiteDatabase db = this.getReadableDatabase();
		return db.rawQuery("select * from " + TABLE_CONVERSATIONS, null);
	}
	
	public Conversation getContact(Cursor cursor) {
		return new Conversation(cursor.getLong(0), cursor.getString(1));
	}
	
	public List<Conversation> getAllConversations() {
		List<Conversation> result = new ArrayList<Conversation>();
		Cursor cursor = selectContacts();
		
		if (cursor.moveToFirst()) {
			do {
				result.add(getContact(cursor));
	        } while (cursor.moveToNext());
		}
		
		return result;
	}
}
