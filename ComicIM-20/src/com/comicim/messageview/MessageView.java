package com.comicim.messageview;

import java.util.ArrayList;
import java.util.List;

import com.comicim.Conversation;
import com.comicim.ConversationListActivity;
import com.comicim.Message;
import com.comicim.contactlist.ConversatonListAdapter;
import com.comicim.smsreceiver.ConversationListener;
import com.comicim.smsreceiver.CoreService;
//import com.example.comicim_20.IntentFilter;
import com.example.comicim_20.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.content.IntentFilter;

public class MessageView extends Activity implements ConversationListener {
	private static final String TAG = "MessageView";
	private CoreService service;
	private final ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(final ComponentName className, final IBinder binder) {
			MessageView.this.service = ((CoreService.LocalBinder) binder).getService();
			MessageView.this.onServiceConnected();
		}

		@Override
		public void onServiceDisconnected(final ComponentName className) {
			MessageView.this.onServiceDisconnected();
		}
	};
	protected void onServiceConnected() {
		Log.i(TAG, "Service connected.");		
		
		service.addListener(this);
		conversation = service.getConversation(conversationId);
		setTitle(conversation.name);
		//contactInfo.setText("Phone: " + conversation.phoneNumber);
		
		group(4);
		
        messageAdapter = new MessageAdapter(this, groupedMessages);
        list.setAdapter(messageAdapter);
        
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //closes keyboard
                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                String message = enterText.getText().toString();
                enterText.setText("");

                //adds message to list
                service.sendMessage(conversation, message);
                group(4);
                messageAdapter.notifyDataSetChanged();
                list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            }	
        });
	}
	protected void onServiceDisconnected() {
		Log.i(TAG, "Service disconnected.");
		
		if (service != null) {
			service.removeListener(this);
			service = null;
		}
	}
	
	List<List<Message>> groupedMessages = new ArrayList<List<Message>>();
	private void group(int size) {
		groupedMessages.clear();
		List<Message> messages = conversation.messages;
		
		for (int i = 0; i < messages.size(); i += size) {
			List<Message> currentGroup = new ArrayList<Message>();
			
			for (int j = 0; j < 4 && (i + j) < messages.size(); j++) {
				currentGroup.add(messages.get(i + j));
			}
			
			groupedMessages.add(currentGroup);
		}
	}
	
	TextView contactInfo = null;
	TextView enterText = null;
	Button sendButton = null;
    TextView charCount = null;
    ListView list = null;
    MessageAdapter messageAdapter = null;
    
    Conversation conversation = null;
    Long conversationId = null;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_view);
        
        Intent intent = getIntent();
        conversationId = intent.getLongExtra(ConversationListActivity.NUMBER, -1);
        
        this.startService(new Intent(this, CoreService.class));
		this.bindService(new Intent(this, CoreService.class), this.serviceConnection, Context.BIND_AUTO_CREATE);
        
		//contactInfo = (TextView)findViewById(R.id.contactInf);
		enterText = (TextView)findViewById(R.id.messageField);
		sendButton = (Button)findViewById(R.id.buttonSend);
        charCount = (TextView) findViewById(R.id.charCount);
        list = (ListView)findViewById(R.id.messageList);
        
        addTextChangedListenerOnEnterText(enterText, charCount);
        registerForContextMenu(list);
    }
	
	public void addTextChangedListenerOnEnterText(TextView enterText,  final TextView charCount ) {
    	enterText.addTextChangedListener(new TextWatcher() {
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onDestroy() {
    	unbindService(serviceConnection);
    	super.onDestroy();
    }
	@Override
	public void onNewConversation(Conversation c) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onNewMessage(Message m) {
		if (m.conversation.id == conversation.id) {
			group(4);
			messageAdapter.notifyDataSetChanged();
		}
	}
}
