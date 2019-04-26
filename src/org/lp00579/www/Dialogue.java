package org.lp00579.www;

import javax.swing.JOptionPane;

public class Dialogue {

	public Dialogue(String msg, boolean close, int delay) {
		super();
		JOptionPane.showMessageDialog(null, msg);
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (close) {
			System.exit(0);
		}
	}
	
	public Dialogue(String errorMsg) {
		super();
		JOptionPane.showMessageDialog(null, errorMsg);
	}

	public Dialogue(String errorMsg, boolean close) {
		super();
		JOptionPane.showMessageDialog(null, errorMsg);
		if (close) {
			System.exit(0);
		}
	}

}
