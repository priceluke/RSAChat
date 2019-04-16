package org.lp00579.www;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.DefaultHighlighter;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import java.awt.Font;

public class ClientPanel extends Operator {
	private String server;
	private JFrame frame;
	private ArrayList<Color> colours;
	private int currentColour;
	private JTextField msgField;
	private JTextArea textArea;
	private JButton btnSend;
	private JLabel lblNowConnected;

	public ClientPanel(String nick, String ip) {
		super(nick);
		colours = new ArrayList<Color>();
		addColourOptions();
		currentColour = 0;
		server = ip;
		initialize();
		Thread sessionThread = new Thread(new Runnable() {
			public void run() {
				initConnection();

			}
		});
		sessionThread.start();
	}

	public void initConnection() {
		try {
			tcpConnection = new Socket(InetAddress.getByName(server), port);

			Outgoing = new ObjectOutputStream(tcpConnection.getOutputStream());
			Outgoing.flush();
			Incoming = new ObjectInputStream(tcpConnection.getInputStream());
			frame.setTitle("Connected to '" + this.server + "'");
			lblNowConnected.setText("Now connected to '" + server + "'");
			session();

		} catch (IOException ioEception) {
			appendUpdate("Failed to connect to " + server);
		}
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
		frame.getContentPane().setBackground(colours.get(currentColour));
	}

	/**
	 * Create the application.
	 */
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setFont(new Font("Courier New", Font.PLAIN, 16));
		frame.setResizable(false);
		frame.setBounds(100, 100, 435, 532);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		msgField = new JTextField();
		msgField.setBounds(15, 426, 268, 26);
		frame.getContentPane().add(msgField);
		msgField.setColumns(10);

		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage(msgField.getText());
				msgField.setText("");
			}
		});
		btnSend.setBounds(298, 425, 115, 29);
		frame.getContentPane().add(btnSend);

		textArea = new JTextArea();
		textArea.setFont(new Font("Courier New", Font.PLAIN, 16));
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setBounds(15, 42, 398, 372);
		frame.getContentPane().add(textArea);

		lblNowConnected = new JLabel("Now connected");
		lblNowConnected.setBounds(15, 16, 398, 20);
		frame.getContentPane().add(lblNowConnected);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JButton btnNewButton = new JButton("Change Nickname");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMyNickname(JOptionPane.showInputDialog(
						"Your nickname is currently '" + myNickname + "'\n\nWhat would you like it to be changed to?",
						myNickname));
			}
		});
		menuBar.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Change Colour");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeColour();
			}
		});
		menuBar.add(btnNewButton_1);

		JButton btnRsa = new JButton("RSA");
		btnRsa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, formatRSADetails());
			}
		});
		menuBar.add(btnRsa);

		frame.setVisible(true);
	}

	private String cropToShow(BigInteger bigInteger_int) {
		String bigInteger = bigInteger_int.toString();
		if (bigInteger.length() > 50) {
			return bigInteger.substring(0, 50) + "...";
		} else {
			return bigInteger;
		}
	}

	private String formatRSADetails() {
		StringBuilder rsa = new StringBuilder("RSA Details:\n\n");
		if (connection_secured) {
			rsa.append("Connection Secured with " + server + "\n\n");
			rsa.append("Your Public Key: \n" + cropToShow(secure.getMyPublicKey()[0])
					+ cropToShow(secure.getMyPublicKey()[1]) + "\n\n");
			rsa.append("Client Public Key: \n" + cropToShow(secure.getConnectedPublicKey()[0])
					+ cropToShow(secure.getConnectedPublicKey()[1]) + "\n\n");
			rsa.append("Your Private Key: \n" + cropToShow(secure.getMyPrivateKey()) + "\n\n");
		} else {
			rsa.append("A connection has not yet been secured...");
		}
		return rsa.toString().replaceAll("(.{100})", "$1\n");
	}

	@Override
	public void appendUpdate(String str) {
		textArea.append(str + "\n");
	}

	@Override
	public void displayMessage(Message message) {
		if (messages.size() > 3) {
			textArea.append("[" + message.getTimeStamp() + "] " + message.getNickname() + ": ");
			textArea.append(message.getMessage() + "\n");
			textArea.repaint();
		}
	}
}
