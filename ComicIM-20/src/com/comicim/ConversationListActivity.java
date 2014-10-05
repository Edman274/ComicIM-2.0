package com.comicim;

import java.util.ArrayList;
import java.util.List;

import com.comicim.contactlist.ConversatonListAdapter;
import com.comicim.contactlist.DatabaseHelper;
import com.comicim.messageview.MessageView;
import com.comicim.smsreceiver.ConversationListener;
import com.comicim.smsreceiver.CoreService;
import com.example.comicim_20.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class ConversationListActivity extends ActionBarActivity implements ConversationListener {
	private static final String TAG = "ContactListActivity";
	private static final int PICK_CONTACT = 1;
	public static final String NUMBER = "com.example.comicim_20";
	
	private CoreService service;
	private final ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(final ComponentName className, final IBinder binder) {
			ConversationListActivity.this.service = ((CoreService.LocalBinder) binder).getService();
			ConversationListActivity.this.onServiceConnected();
		}

		@Override
		public void onServiceDisconnected(final ComponentName className) {
			ConversationListActivity.this.onServiceDisconnected();
		}
	};
	protected void onServiceConnected() {
		Log.i(TAG, "Service connected.");
		
		service.addListener(this);
		
		contactListViewAdapter = new ConversatonListAdapter(this, service.conversations);
        contactListView.setAdapter(contactListViewAdapter);
        
        registerForContextMenu(contactListView);
        
        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(view.getContext(), MessageView.class);
				intent.putExtra(NUMBER, service.conversations.get(position).id);
				startActivity(intent);
			}
        });
	}
	protected void onServiceDisconnected() {
		Log.i(TAG, "Service disconnected.");
		
		if (this.service != null) {
			this.service.removeListener(this);
			this.service = null;
		}
	}
	
	public ListView contactListView;
	public ConversatonListAdapter contactListViewAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate.");
        setContentView(R.layout.contact_list_activity);
        
        this.startService(new Intent(this, CoreService.class));
		this.bindService(new Intent(this, CoreService.class), this.serviceConnection, Context.BIND_AUTO_CREATE);
        
        contactListView = (ListView) this.findViewById(R.id.contact_list);
    }
    
    @Override
    protected void onDestroy() {
    	unbindService(serviceConnection);
    	super.onDestroy();
    }

    @Override
	public void onNewConversation(Conversation c) {
    	Log.i(TAG, "onNewConversation");
    	this.contactListViewAdapter.notifyDataSetChanged();
	}
	@Override
	public void onNewMessage(Message m) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add_conversation || item.getTitle().toString().equals("Add contact")){
        	Intent contactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			contactIntent.setType(Phone.CONTENT_TYPE); // This line is important! 
			startActivityForResult(contactIntent, PICK_CONTACT);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
    	Log.i(TAG, "onActivityResult.");
		super.onActivityResult(requestCode, resultCode, data);
			switch(requestCode) {
			case(PICK_CONTACT):
				if(resultCode == Activity.RESULT_OK) {
					Uri contactUri = data.getData();
					String[] projection = {Phone.NUMBER};
					Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
					cursor.moveToFirst();
					int column = cursor.getColumnIndex(Phone.NUMBER);
					
					String phoneNumber = PhoneNumberUtils.stripSeparators(cursor.getString(column));
					Conversation contact = this.service.database.newContact(phoneNumber);
					service.conversations.add(contact);
					this.onNewConversation(contact);
				}
			}
	}
    
    public void onCreateContextMenu(ContextMenu menu, View list, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, list, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        menu.setHeaderTitle("Actions");
        menu.add(0, list.getId(), 0, "Remove");

    }
    
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().toString().equals("Remove")) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            this.service.database.deleteConversation(contactListViewAdapter.getItem(info.position).phoneNumber);
            service.conversations.remove(contactListViewAdapter.getItem(info.position));
            contactListViewAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "Conversation deleted.", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (item.getTitle().toString().equals("Add contact")) {
        	return onOptionsItemSelected(item);
        } else {
            return false;
        }
    }
}
