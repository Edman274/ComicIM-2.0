package com.example.comicim_20.smsreceiver;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver{
	private Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        this.context = context;

        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for(Object currentObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) currentObj);
                    
                    Log.d("MessageRecv", currentMessage.getDisplayMessageBody());
                    /**Message message = new Message(
                            currentMessage.getDisplayMessageBody(),
                            currentMessage.getDisplayOriginatingAddress(),
                            "ME",
                            new Date()
                    );**/
                    //DataProvider.getInstance().addMessage(message);
                }
                sendMessage();
            }
        } catch (Exception e) {
            Log.e("SMS", "Exception: " + e);
        }
    }
	
	private void sendMessage() {
		  Log.d("sender", "Broadcasting message12345");
		  Intent intent = new Intent("update-messages");
		  // You can also include some extra data.
		  intent.putExtra("message", "This is my message!");
		  //LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

}
