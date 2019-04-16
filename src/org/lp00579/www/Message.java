package org.lp00579.www;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message  implements Serializable {
	public void setMessage(String message) {
		this.message = message;
	}

	private String nickname;
	private String message;
	private String timeStamp;

	public Message(String nickname, String message) {
		super();
		this.nickname = nickname;
		this.message = message;
		this.timeStamp = new SimpleDateFormat("HH.mm.ss").format(new Date());
	}

	public String getNickname() {
		return nickname;
	}

	public String getMessage() {
		return message;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

}
