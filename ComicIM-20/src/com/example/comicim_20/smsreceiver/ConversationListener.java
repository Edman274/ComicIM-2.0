package com.example.comicim_20.smsreceiver;

import com.example.comicim_20.Conversation;
import com.example.comicim_20.Message;

public interface ConversationListener {
	public void onNewConversation(Conversation c);
	public void onNewMessage(Message m);
}
