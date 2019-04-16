package org.lp00579.www;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Operator {
	private String server;

	public Client(String myNickname, String hostIP) {
		super(myNickname);
		server = hostIP;
		try {
			initConnection();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void initConnection() throws InterruptedException {
		try {
			try {
				tcpConnection = new Socket(InetAddress.getByName(server), port);
			} catch (IOException ioEception) {
				System.out.println("FUCKING FAILED TO CONNECT :(" + this.myNickname);
			}

			Outgoing = new ObjectOutputStream(tcpConnection.getOutputStream());
			Outgoing.flush();
			Incoming = new ObjectInputStream(tcpConnection.getInputStream());

			session();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
