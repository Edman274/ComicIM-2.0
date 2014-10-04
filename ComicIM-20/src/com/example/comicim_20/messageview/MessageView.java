package com.example.comicim_20.messageview;

import java.util.ArrayList;

import com.example.comicim_20.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;



public class MessageView extends Activity{
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_view);
        
        Intent intent = getIntent();
        
        final EditText enterPhone = (EditText)findViewById(R.id.enterPhone);
        final TextView enterText = (TextView)findViewById(R.id.enterText);
        final Button sendButton = (Button)findViewById(R.id.sendButton);

        final ArrayList<String> messageList = new ArrayList<String>();
        final ListView list = (ListView)findViewById(R.id.mList);
        final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messageList);
        list.setAdapter(listAdapter);
        registerForContextMenu(list);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //closes keyboard
                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                //gets number and message text
                String phoneNumber = enterPhone.getText().toString().trim();
                String message = enterText.getText().toString().trim();

                sendMessage(phoneNumber, message);

                //resets message field
                enterText.setText("");

                //adds message to list
                messageList.add(message);
                listAdapter.notifyDataSetChanged();
            }	
        });
        
    }
	
	private static void sendMessage(String phoneNumber, String message) {
        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(phoneNumber, null, message, null, null);
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
}