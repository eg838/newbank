package newbank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewBankClientHandler extends Thread {

	private NewBank bank;
	private BufferedReader in;
	private PrintWriter out;

	public NewBankClientHandler(Socket s) throws IOException {
		bank = NewBank.getBank();
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new PrintWriter(s.getOutputStream(), true);
	}

	public void printOut(String s) {
		try {
			out.println(s);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	public String getInput() {
		try {
			return in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void run() {
		// keep getting requests from the client and processing them
		try {
			while (true) {
				// ask for user name
				out.println("Enter Username");
				String userName = in.readLine();
				// ask for password
				out.println("Enter Password");
				String password = in.readLine();
				out.println("Checking Details...");
				// authenticate user and get customer ID token from bank for use in subsequent requests
				CustomerID customer = bank.checkLogInDetails(userName, password);
				// if the user is authenticated then get requests from the user and process them 
				if(customer != null) {

					out.println("Log In Successful.What do you want to do?");
					out.println();

					/* add options to show accounts and move. Other options to be added later on
					out.printf("Choose an option below by entering the number against it to continue: %n" +
							"1: SHOWMYACCOUNTS %n" +
							"2: MOVE %n" +
							"3: NEWBANK Savings %n" +
							"4: NEWBANK Checking");  */

					out.println("Choose an option below by entering the number against it to continue:");
					out.println("1: SHOWMYACCOUNTS");
					out.println("2: MOVE");
					out.println("3: NEWBANK Savings");
					out.println("4: NEWBANK Checking");


					while(true) {

						String request = "";
						int option  = Integer.parseInt(in.readLine());

						switch(option){
							case 1:
								request = "SHOWMYACCOUNTS";
										break;
							case 2:
								request = "MOVE";
								break;

							case 3:
								request = "NEWBANK Savings";
								break;

							case 4:
								request = "NEWBANK Checking";
								break;
						}

						System.out.println("Request from " + customer.getKey());
						String response = bank.processRequest(customer, request);
						out.println(response);
					}
				}else {
 					out.println("Log In Failed");
 				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

}
