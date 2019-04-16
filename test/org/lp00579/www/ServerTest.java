package org.lp00579.www;

import java.util.Scanner;

public class ServerTest {

		public static void main(String[] args) {
			System.out.println("--- SERVER HAS BEEN STARTED ---");
			Server S1 = new Server("Server");

			Thread thread3 = new Thread(new Runnable() {
	            @Override
	            public void run() {
	                 try {
						S1.listenConnection();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                // We are doing the same thing as with the MyRunnableImplementation class

	            }
	        }, "Thread 3");
	        thread3.start();
	        do {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        } while (S1.connection_secured == false);
			
			String text_in = "";
			do {
				Scanner scanner = new Scanner(System.in);
				text_in = scanner.nextLine();
				S1.sendMessage(text_in);
			} while (!text_in.equals("end"));
			S1.closeConnection();

		}
}
