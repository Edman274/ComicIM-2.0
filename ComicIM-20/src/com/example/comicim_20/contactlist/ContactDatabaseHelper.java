package com.example.comicim_20.contactlist;

import java.util.ArrayList;
import java.util.List;

import com.example.comicim_20.Conversation;
import com.example.comicim_20.Message;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class ContactDatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = ContactDatabaseHelper.class.getPackage().getName();
	
    private static final String TABLE_CONVERSATIONS = "conversations";
    private static final String TABLE_MESSAGES = "messages";
    
    private static final String CREATE_TABLE_CONVERSATIONS = 
    		"create table " + TABLE_CONVERSATIONS + "("
    		+ "id integer primary key, "
    		+ "phone_number text"
    		+ ")";
    private static final String CREATE_TABLE_MESSAGES = 
    		"create table " + TABLE_MESSAGES + "("
    		+ "id integer primary key, "
    		+ "conversation_id integer, "
    		+ "from_me integer, "
    		+ "message_text text"
    		+ ")";
    
	public ContactDatabaseHelper(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_CONVERSATIONS);
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
	
	public List<Conversation> getAllConversations() {
		List<Conversation> result = new ArrayList<Conversation>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + TABLE_CONVERSATIONS, null);
		
		if (cursor.moveToFirst()) {
			do {
				result.add(new Conversation(cursor.getLong(0), cursor.getString(1)));
	        } while (cursor.moveToNext());
		}
		
		return result;
	}
	
	public void deleteConversation(String number) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONVERSATIONS, "phone_number=" + number, null);
	}
	
	public List<Message> getAllMessages(Conversation conversation) {
		List<Message> result = new ArrayList<Message>();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from " + TABLE_MESSAGES + " where conversation_id=" + conversation.id, null);
		
		if (cursor.moveToFirst()) {
			do {
				result.add(new Message(
						cursor.getLong(0),
						conversation,
						cursor.getLong(2) != 0,
						cursor.getString(3)));
	        } while (cursor.moveToNext());
		}
		
		return result;
	}
	
	public Message addMessage(Conversation conversation, boolean fromMe, String text) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put("conversation_id", conversation.id);
		cv.put("from_me", fromMe ? 1 : 0);
		cv.put("message_text", text);
		
		long id = db.insert(TABLE_MESSAGES, null, cv);
		return new Message(id, conversation, fromMe, text);
	}
}
