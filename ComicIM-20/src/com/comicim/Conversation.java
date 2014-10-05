package com.comicim;

import java.util.ArrayList;
import java.util.List;

public final class Conversation {
	public Conversation(long id, String phoneNumber, String name, byte[] picture) {
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.name = name;
		this.messages = new ArrayList<Message>();
		this.picture = picture;
	}

	public long id;
	public String phoneNumber;
	public String name;
	public byte[] picture;
	public List<Message> messages;
}
