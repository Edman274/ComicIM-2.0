package com.example.comicim_20.messageview;

import java.util.ArrayList;

import com.example.comicim_20.ContactListActivity;
//import com.example.comicim_20.IntentFilter;
import com.example.comicim_20.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.content.IntentFilter;



public class MessageView extends Activity{
	
	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
    	@Override
    	public void onReceive(Context context, Intent intent) {
    	    // Get extra data included in the Intent
    	    String message = intent.getStringExtra("message");
    	    Log.d("receiver", "Got message:");
    	  }
    };
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_view);
        
        Intent intent = getIntent();
        final String num = intent.getStringExtra(ContactListActivity.NUMBER);
        
		final TextView contactInfo = (TextView)findViewById(R.id.contactInf);
		final TextView enterText = (TextView)findViewById(R.id.messageField);
		final Button sendButton = (Button)findViewById(R.id.buttonSend);
        final TextView charCount = (TextView) findViewById(R.id.charCount);


        contactInfo.setText(num);
        
        final ArrayList<String> messageList = new ArrayList<String>();
        final ListView list = (ListView)findViewById(R.id.messageList);
        final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messageList);
        addTextChangedListenerOnEnterText(enterText, charCount);
        list.setAdapter(listAdapter);
        registerForContextMenu(list);
        
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("update-messages"));
        
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //closes keyboard
                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                //gets message text
                String message = enterText.getText().toString().trim();

                sendMessage(num, message);

                //resets message field
                enterText.setText("");

                //adds message to list
                messageList.add(message);
                listAdapter.notifyDataSetChanged();
            }	
        });
        
    }
	
	public void addTextChangedListenerOnEnterText(TextView enterText,  final TextView charCount ){
		
    	enterText.addTextChangedListener(new TextWatcher()
		{
		    public void afterTextChanged(Editable s) 
		    {
				int messageLength[] = SmsMessage.calculateLength(s.toString(), false);
				charCount.setText(Integer.toString(messageLength[2]));
				if(messageLength[0] > 1) {
					charCount.append("[" + Integer.toString(messageLength[0] - 1) + "]");
					
				}
		    }
		    public void beforeTextChanged(CharSequence s, int start, int count, int after) 
		    {
		        /*This method is called to notify you that, within s, the count characters beginning 
		         * at start are about to be replaced by new text with length after. 
		         * It is an error to attempt to make changes to s from this callback.*/ 
		    }
		    public void onTextChanged(CharSequence s, int start, int before, int count) { }
		  
		}
    			
    );}
	
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    protected void onDestroy() {
    	  // Unregister since the activity is about to be closed.
    	  LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    	  super.onDestroy();
    }
}
