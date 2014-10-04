package com.example.comicim_20.smsreceiver;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();

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
            }
        } catch (Exception e) {
            Log.e("SMS", "Exception: " + e);
        }
    }

}
