package org.lp00579.www;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

public class Server extends Operator {
	private ServerSocket server;

	public Server(String myNickname) {
		super(myNickname);
		try {
			listenConnection();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void listenConnection() throws InterruptedException {
		System.out.println("--- WAITING FOR CLIENT CONNECTION ---");
		try {
			server = new ServerSocket(port, 2);
			while (true) {
				try {
					this.tcpConnection = server.accept();
					System.out.println("--- TCP CONNECTION ESTABLISHED ---");
					Outgoing = new ObjectOutputStream(tcpConnection.getOutputStream());
					Outgoing.flush();
					Incoming = new ObjectInputStream(tcpConnection.getInputStream());
					session();

				} catch (EOFException eofException) {
				}
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
