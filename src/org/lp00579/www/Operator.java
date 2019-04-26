package org.lp00579.www;

import java.awt.List;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;


public abstract class Operator {
	protected ObjectOutputStream Outgoing;
	protected ObjectInputStream Incoming;
	protected EncryptionHandler secure;
	protected String myNickname;
	protected ArrayList<Message> messages;
	protected Socket tcpConnection;
	protected int port;
	protected boolean connection_secured;
	protected String handshake;


	public Operator(String myNickname) {
		super();
		this.myNickname = myNickname;
		this.handshake = "HorEyGl21Z9OGcS3XccUgQimYMwF0XskyH3wPQeRhHClvkGlNAuyraYokZddsBrGzMEl5UQolrlYQAmpWdpeaqj2uYHJ2McepLX8";
		this.connection_secured = false;
		this.messages = new ArrayList<Message>();
		this.port = 6789;
		this.secure = new EncryptionHandler();
	}

	public boolean sendMessage(String body) {

		Message msg = new Message(myNickname, body);
		displayMessage(msg);
		msg.setMessage(secure.internalEncrypt(body));
		try {
			Outgoing.writeObject(msg);
			messages.add(msg);
			return true;

		} catch (IOException ioException) {
			return false;
		}
	}

	public boolean sendKey(String body) {
		Message msg = new Message(myNickname, body);
		try {
			Outgoing.writeObject(msg);
			messages.add(msg);
			return true;

		} catch (IOException ioException) {
			return false;
		}
	}

	public void session()  {
		sendKey(secure.getMyPublicKey()[0] + " " + secure.getMyPublicKey()[1]);
		Message message = null;
		int i = 0;
		do {
			try {
				message = ((Message) Incoming.readObject());
				recivedMessage(message);
			} catch (ClassNotFoundException classNotFoundException) {

			} catch (IOException e) {
		
			}
			i++;
		} while (!message.getMessage().equals("END"));
		this.closeConnection();
	}

	public void recivedMessage(Message message) {
		messages.add(message);
		if (messages.size() > 4) {
			message.setMessage(secure.decrypt(message.getMessage()));
			displayMessage(message);
		} else if (!message.getNickname().equals(myNickname)) {
			if (messages.size() == 2 || messages.size() == 1) {
				appendUpdate("--- REMOTE PUBLIC KEY SET ---");
				String[] rem_pub_str = message.getMessage().split("\\s+");
				BigInteger[] rem_pub = { new BigInteger(rem_pub_str[0]), new BigInteger(rem_pub_str[1]) };
				secure.setConnectedPublicKey(rem_pub);
				initHandshake();
			} else if (messages.size() == 3 || messages.size() == 4) {
				checkHandshake(message);
			}
		}
	}

	public void displayMessage(Message message) {
		if (messages.size() > 4) {
			appendUpdate(message.getTimeStamp() + " - '" + message.getNickname() + "': " + message.getMessage());
		}
	}

	public void initHandshake() {
		// constructMessage(secure.encrypt_Client("HELLO WORLD"));
		appendUpdate("--- SENDING HANDSHAKE ---");
		sendMessage(handshake);
	}

	public void checkHandshake(Message msg) {
		if (secure.decrypt(msg.getMessage()).equals(handshake)) {
			appendUpdate("--- RSA KEYS VERIFIED ---");
			connection_secured = true;
		}

	}

	public void closeConnection() {
		appendUpdate("--- CONNECTION CLOSED ---");
		try {
			Incoming.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setMyNickname(String myNickname) {
		this.myNickname = myNickname;
	}

	public String getMessages() {
		StringBuilder chatHistory = new StringBuilder();
		for (Message msg : messages) {
			chatHistory.append(msg.getMessage() + "\n");
		}
		return chatHistory.toString();
	}
	
	public void appendUpdate(String str) {}
	}
