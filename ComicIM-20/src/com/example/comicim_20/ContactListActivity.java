package com.example.comicim_20;

import java.util.ArrayList;
import java.util.List;

import com.example.comicim_20.contactlist.ContactDatabaseHelper;
import com.example.comicim_20.contactlist.ContactListAdapter;
import com.example.comicim_20.messageview.MessageView;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
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


public class ContactListActivity extends ActionBarActivity {
	private static String TAG = ContactListActivity.class.getName();
	
	public ContactDatabaseHelper databaseHelper;
	public List<Conversation> conversations;
	public ListView contactListView;
	public ContactListAdapter contactListViewAdapter;
	private final int PICK_CONTACT = 1;
	final public static String NUMBER = "com.example.comicim_20";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list_activity);
                
        contactListView = (ListView) this.findViewById(R.id.contact_list);
        
        databaseHelper = new ContactDatabaseHelper(this);
        conversations = databaseHelper.getAllConversations();
        
        contactListViewAdapter = new ContactListAdapter(this, conversations);
        contactListView.setAdapter(contactListViewAdapter);
        
        registerForContextMenu(contactListView);
        
        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.i(TAG, "Clicked on " + conversations.get(position).phoneNumber);
				Intent intent = new Intent(view.getContext(), MessageView.class);
				intent.putExtra(NUMBER, conversations.get(position).phoneNumber);
				startActivity(intent);
			}
        });
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
					
					boolean match = false;
					for (int i = 0; i < conversations.size(); i++) {
						Conversation item = conversations.remove(i);
						if (item.phoneNumber.equals(phoneNumber)) {
							match = true;
						}
						conversations.add(item);
					}
					
					if (!match) {
						Conversation contact = this.databaseHelper.newContact(phoneNumber);
						conversations.add(contact);
						contactListViewAdapter.notifyDataSetChanged();
					} else {
						Toast.makeText(getApplicationContext(), "Conversation already exists!", Toast.LENGTH_SHORT).show();
					}
					
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
            contactListViewAdapter.remove(contactListViewAdapter.getItem(info.position));
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
