package com.example.sms_test;

import android.R;
import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final EditText enterPhone = (EditText)findViewById(R.id.enterPhone);
        final EditText enterText = (EditText)findViewById(R.id.enterText);
        final Button sendButton = (Button)findViewById(R.id.sendButton);
        
        sendButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phoneNumber = enterPhone.getText().toString().trim();
				String message = enterText.getText().toString().trim();
				sendMessage(phoneNumber, message);
			}
		}); {
        	
        }
        
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
