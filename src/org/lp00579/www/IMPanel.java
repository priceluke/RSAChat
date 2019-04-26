package org.lp00579.www;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class IMPanel {

	private JFrame frmConnect;
	private JTextField nick;
	private ArrayList<String[]> recentConnections;
	private JTextField ip;
	private String savedPath;
	private JComboBox comboBox;

	private void genRecentConnections(final File folder) {
		comboBox.addItem(new comboItem("Select from contact list", ""));

		if (folder.exists()) {
			for (final File fileEntry : folder.listFiles()) {
				System.out.println(fileEntry.getName());
				comboBox.addItem(new comboItem(fileEntry.getName().replaceFirst("[.][^.]+$", ""), getFileContents(fileEntry)));
			}
		} else {
			folder.mkdir();
		}
	}
	private String getFileContents(File file) {
		try( FileReader fileStream = new FileReader( file ); 
		    BufferedReader bufferedReader = new BufferedReader( fileStream ) ) {

		    String line = null;

		    while( (line = bufferedReader.readLine()) != null ) {
		    	return line;
		    }

		    } catch ( FileNotFoundException ex ) {
		    } catch ( IOException ex ) {
		}
    	return "failed to find";

	}
	/**
	 * Create the application.
	 */
	public IMPanel() {
		initialize();

		savedPath = System.getenv("APPDATA") + "\\RSACHAT";
		final File folder = new File(savedPath);
		System.out.println(folder.toString());
		genRecentConnections(folder);
		recentConnections = new ArrayList<String[]>();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmConnect = new JFrame();
		frmConnect.setTitle("Connect");
		frmConnect.setBounds(100, 100, 362, 238);
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
		btnConnect.setBounds(210, 102, 115, 29);
		frmConnect.getContentPane().add(btnConnect);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveConn();
			}
		});
		btnSave.setBounds(15, 102, 115, 29);
		frmConnect.getContentPane().add(btnSave);

		comboBox = new JComboBox();
		comboBox.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
				comboItem ci = (comboItem) comboBox.getSelectedItem();
				ip.setText(ci.getValue());
			}
		});
		comboBox.setBounds(15, 146, 310, 26);
		frmConnect.getContentPane().add(comboBox);
		frmConnect.setVisible(true);

	}

	private void saveConn() {
		String remote_nickname = JOptionPane.showInputDialog("Please enter an identifier for this remote host: ");

		File file = new File(savedPath + "\\" + remote_nickname + ".txt");

		// Create the file
		try {
			if (file.createNewFile()) {
				System.out.println("File is created!");
			} else {
				System.out.println("File already exists.");
			}

			// Write Content
			FileWriter writer = new FileWriter(file);
			writer.write(ip.getText());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final File folder = new File(savedPath);
		genRecentConnections(folder);
	}
}
