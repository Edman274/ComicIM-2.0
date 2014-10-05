package com.comicim;

public final class Message {
	public Message(long id, Conversation conversation, boolean fromMe, String text) {
		this.id = id;
		this.conversation = conversation;
		this.fromMe = fromMe;
		this.text = text;
	}
	
	public long id;
	public Conversation conversation;
	public boolean fromMe;
	public String text;
}
