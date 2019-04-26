package org.lp00579.www;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.List;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

public class ConnectionPanel extends Operator {
	private JFrame frmChatConnection;
	private JTextField messageField;
	private JTextArea chatArea;
	private ServerSocket server;
	private JButton btnConnect;
	private JLabel lblNowConnected;
	private JButton btnChangeNickname;
	private ArrayList<Color> colours;
	private int currentColour;
	private boolean clientConnectShown;
	private JButton btnRsa;

	/**
	 * Create the application.
	 */
	public ConnectionPanel(String name) {
		super(name);
		clientConnectShown = false;
		colours = new ArrayList<Color>();
		addColourOptions();
		currentColour = 0;
		initialize();
		listenConnection();
	}

	private void addColourOptions() {
		colours.add(Color.RED);
		colours.add(Color.BLUE);
		colours.add(Color.CYAN);
		colours.add(Color.YELLOW);
		colours.add(Color.GRAY);
		colours.add(Color.LIGHT_GRAY);
		colours.add(Color.WHITE);
		colours.add(Color.PINK);
	}

	private void changeColour() {
		if (currentColour < colours.size() - 1) {
			currentColour++;
		} else {
			currentColour = 0;
		}
		frmChatConnection.getContentPane().setBackground(colours.get(currentColour));
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChatConnection = new JFrame();
		frmChatConnection.setResizable(false);
		frmChatConnection.setTitle("EChat");
		frmChatConnection.setBounds(100, 100, 435, 129);
		frmChatConnection.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmChatConnection.getContentPane().setLayout(null);

		btnConnect = new JButton("Connect to Client");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				initReq();
			}
		});
		btnConnect.setBounds(142, 9, 153, 35);
		frmChatConnection.getContentPane().add(btnConnect);

		messageField = new JTextField();
		messageField.setBounds(15, 480, 308, 26);
		frmChatConnection.getContentPane().add(messageField);
		messageField.setColumns(10);

		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendMessage(messageField.getText());
				messageField.setText("");
			}
		});
		btnSend.setBounds(329, 479, 85, 29);
		frmChatConnection.getContentPane().add(btnSend);

		chatArea = new JTextArea();
		chatArea.setLineWrap(true);
		chatArea.setEditable(false);
		chatArea.setVisible(false);
		chatArea.setBounds(15, 40, 399, 432);
		frmChatConnection.getContentPane().add(chatArea);

		lblNowConnected = new JLabel("Now Connected");
		lblNowConnected.setBounds(15, 16, 399, 20);
		lblNowConnected.setVisible(false);
		frmChatConnection.getContentPane().add(lblNowConnected);

		JMenuBar menuBar = new JMenuBar();
		frmChatConnection.setJMenuBar(menuBar);

		btnChangeNickname = new JButton("Change Nickname");
		btnChangeNickname.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setMyNickname(JOptionPane.showInputDialog(
						"Your nickname is currently '" + myNickname + "'\n\nWhat would you like it to be changed to?",
						myNickname));
			}
		});
		menuBar.add(btnChangeNickname);

		JButton btnChangeBackgroundColour = new JButton("Change Background Colour");
		btnChangeBackgroundColour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeColour();
			}
		});
		menuBar.add(btnChangeBackgroundColour);

		btnRsa = new JButton("RSA");
		btnRsa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Dialogue("Currently connected to '"+ server + "'\n\n"+  secure.display());
			}

		});
		menuBar.add(btnRsa);

		frmChatConnection.setVisible(true);
	}


	public void listenConnection() {
		appendUpdate("--- WAITING FOR CLIENT CONNECTION ---");
		try {
			server = new ServerSocket(port, 2);
			while (true) {
				try {
					this.tcpConnection = server.accept();
					appendUpdate("--- TCP CONNECTION ESTABLISHED ---");
					Outgoing = new ObjectOutputStream(tcpConnection.getOutputStream());
					Outgoing.flush();
					Incoming = new ObjectInputStream(tcpConnection.getInputStream());
					frmChatConnection.setSize(435, 590);
					chatArea.setVisible(true);
					btnConnect.setVisible(false);
					lblNowConnected.setVisible(true);
					lblNowConnected.setText("Currently Connected to '" + tcpConnection.getLocalAddress() + "'");
					session();

				} catch (EOFException eofException) {
					// loop until server is requested
				}
			}
		} catch (IOException ioException) {
			if (!clientConnectShown) {
				new Dialogue(
						"An error has occured, it appears there is another program blocking the port in use.\n\nThis instance will now close.\n\nIf you want to run two instances for testing, please navigate to the connection screen before opening a second chat.",
						true, 500);
			} else {
				frmChatConnection.setVisible(false);
			}
		}
	}

	public void initReq() {
		clientConnectShown = true;

		try {
			server.close();
		} catch (IOException e) {
			// already closed
		}
		server = null;
		frmChatConnection.setVisible(false);
		IMPanel newCP = new IMPanel();
	}

	@Override
	public void appendUpdate(String str) {
		chatArea.append(str + "\n");
	}

	@Override
	public void displayMessage(Message message) {
		if (messages.size() > 3) {
			chatArea.append(
					"\n" + message.getTimeStamp() + " - '" + message.getNickname() + "': " + message.getMessage());
			chatArea.repaint();
		}
	}
}
