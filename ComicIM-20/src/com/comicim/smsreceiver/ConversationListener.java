package com.comicim.smsreceiver;

import com.comicim.Conversation;
import com.comicim.Message;

public interface ConversationListener {
	public void onNewConversation(Conversation c);
	public void onNewMessage(Message m);
}
