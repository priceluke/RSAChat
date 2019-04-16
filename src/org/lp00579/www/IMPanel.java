package org.lp00579.www;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class IMPanel {

	private JFrame frmConnect;
	private JTextField nick;
	private JTextField ip;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IMPanel window = new IMPanel();
					window.frmConnect.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public IMPanel() {

		initialize();
	
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmConnect = new JFrame();
		frmConnect.setTitle("Connect");
		frmConnect.setBounds(100, 100, 362, 208);
		frmConnect.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmConnect.getContentPane().setLayout(null);
		
		nick = new JTextField();
		nick.setToolTipText("Nickname");
		nick.setBounds(15, 18, 310, 26);
		frmConnect.getContentPane().add(nick);
		nick.setColumns(10);
		
		ip = new JTextField();
		ip.setToolTipText("IP Address");
		ip.setBounds(15, 60, 310, 26);
		frmConnect.getContentPane().add(ip);
		ip.setColumns(10);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmConnect.setVisible(false);
				ClientPanel cp1 = new ClientPanel(nick.getText(), ip.getText());
			}
		});
		btnConnect.setBounds(121, 102, 115, 29);
		frmConnect.getContentPane().add(btnConnect);
		frmConnect.setVisible(true);

	}
}
