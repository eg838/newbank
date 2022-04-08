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
				// instantiate an user session to tracks login attempts
				UserSession userS = new UserSession(userName);
				// if the user is authenticated then get requests from the user and process them
				if (customer != null) {
					out.println("Log In Successful. What do you want to do?");
					while (true) {
						String request = in.readLine();
						System.out.println("Request from " + customer.getKey());
						String response = bank.processRequest(customer, request);
						out.println(response);
					}
				} else {
					out.println("Log In Failed. Username or password is incorrect!");
					userS.incrementAttempt();
					if (userS.getAttempts() > 3 && userS.getUsername() == userName) {
						throw new Exception("You tried too many times so you can't log in with that username!");
					}
				}
			}
		} catch (Exception e) {
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
