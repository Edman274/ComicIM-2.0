package com.comicim.smsreceiver;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.comicim.Conversation;
import com.comicim.Message;
import com.comicim.contactlist.DatabaseHelper;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public final class CoreService extends Service {
	private static final String TAG = "CoreService";
	
	private final LocalBinder binder = new LocalBinder(this);
	private final SmsReceiver receiver = new SmsReceiver(this);
	
	public DatabaseHelper database;
	public List<Conversation> conversations;
	
	public List<ConversationListener> listeners = new ArrayList<ConversationListener>();
	
	String name = null;
	String picture = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate");
		
		database = new DatabaseHelper(this);
        conversations = database.getAllConversations();
        
        for (Conversation c : conversations) {
        	c.messages = database.getAllMessages(c);
        }
		
		IntentFilter filter = new IntentFilter();
		filter.setPriority(1000);
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		this.registerReceiver(receiver, filter);
	}
	
	@Override
	public IBinder onBind(final Intent intent) {
		Log.i(TAG, "onBind");
		return this.binder;
	}
	
	public void addListener(ConversationListener listener) {
		this.listeners.add(listener);
	}
	public void removeListener(ConversationListener listener) {
		this.listeners.remove(listener);
	}
	
	public void onMessage(String sender, String body) {
		Log.i(TAG, "onMessage");
		
		Conversation conversation = getConversation(sender);
		
		Log.i(TAG, "onMessage" + conversation.id + " " + sender + " " + conversation.phoneNumber);
		
		Message msg = database.addMessage(conversation, false, body);
		conversation.messages.add(msg);
		
		for (ConversationListener listener : listeners) {
			listener.onNewMessage(msg);
		}
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}

	public static final class LocalBinder extends Binder {
		private final WeakReference<CoreService> service;

		LocalBinder(final CoreService networkingService) {
			this.service = new WeakReference<CoreService>(networkingService);
		}
		public CoreService getService() {
			return this.service.get();
		}
	}
	
	public static final class SmsReceiver extends BroadcastReceiver {
		private final WeakReference<CoreService> service;
		
		SmsReceiver(final CoreService service) {
			this.service = new WeakReference<CoreService>(service);
		}
		
		@Override
		public void onReceive(Context context, Intent intent) {
	        final Bundle bundle = intent.getExtras();

	        try {
	            if (bundle != null) {
	                final Object[] pdusObj = (Object[]) bundle.get("pdus");

	                for(Object currentObj : pdusObj) {
	                    SmsMessage message = SmsMessage.createFromPdu((byte[]) currentObj);
	                    String body = message.getMessageBody().toString();
	                    String sender = message.getOriginatingAddress();
	                    this.service.get().onMessage(sender, body);
	                }
	            }
	        } catch (Exception e) {
	            Log.e("SMS", "Exception: " + e);
	        }
	    }

	}
	
	private static void sendMessage(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
		int messageLength[] = SmsMessage.calculateLength(message, false);

        if(phoneNumber.length() > 0 && message.length() > 0 && messageLength[0] == 1){
        	smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        }
        
        else if (phoneNumber.length() > 0 && messageLength[0] > 1){
			ArrayList<String> messageFragments = smsManager.divideMessage(message);
			smsManager.sendMultipartTextMessage(phoneNumber, null, messageFragments, null, null);
		}
    }

	public void sendMessage(Conversation conversation, String body) {
		Log.i(TAG, "sendMessage");
		Message msg = database.addMessage(conversation, true, body);
		conversation.messages.add(msg);
		sendMessage(conversation.phoneNumber, body);
	}
	
	public Conversation getConversation(long id) {
		Log.i(TAG, "getConversation");
		
		Conversation conversation = null;
		for (Conversation c : conversations) {
			if (c.id == id) {
				conversation = c;
			}
		}
		return conversation;
	}

	public Conversation getConversation(String phoneNumber) {
		Log.i(TAG, "getConversation");
		Conversation conversation = null;
		for (Conversation c : conversations) {
			if (c.phoneNumber.equals(phoneNumber)) {
				conversation = c;
			}
		}
		if (conversation == null) {
			conversation = database.newContact(phoneNumber, "", null);
			conversations.add(conversation);
			
			for (ConversationListener listener : listeners) {
				listener.onNewConversation(conversation);
			}
		}
		
		return conversation;
	}
}
