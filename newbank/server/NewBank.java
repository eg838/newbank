package newbank.server;

import java.util.HashMap;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.IOException;




public class NewBank {

	private static final NewBank bank = new NewBank();
	private HashMap<String,Customer> customers;
	private NewBankClientHandler newBankClientHandler;
	
	private NewBank() {
		customers = new HashMap<>();
		addTestData();
	}

	private void addTestData() {
		Customer bhagy = new Customer();
		bhagy.addAccount(new Account("Main", 1000.0));
		bhagy.registerPW("PWtest1");
		customers.put("Bhagy", bhagy);

		Customer christina = new Customer();
		christina.addAccount(new Account("Savings", 1500.0));
		christina.registerPW("PWtest2");
		customers.put("Christina", christina);

		Customer john = new Customer();
		john.addAccount(new Account("Checking", 250.0));
		john.registerPW("PWtest3");
		customers.put("John", john);
	}

	public static NewBank getBank() {
		return bank;
	}

	public synchronized CustomerID checkLogInDetails(String userName, String password) {
		if (customers.containsKey(userName)) {
			if (customers.get(userName).getPW().equals(password)) {
				return new CustomerID(userName);
			}
		}
		return null;
	}

	// commands from the NewBank customer are processed in this method
	public synchronized String processRequest(CustomerID customer, String request) {
		if(customers.containsKey(customer.getKey())) {
			switch(request) {
			case "SHOWMYACCOUNTS" : return showMyAccounts(customer);
			case "NEWBANK Savings" : return createAccount(customer,"Savings");
			case "NEWBANK Checking" : return createAccount(customer,"Checking");
			case "NEWBANK Main" : return createAccount(customer,"Main");
			case "MOVE" : return move(customer);
			}
		}
		return "Incorrect Command Entered";
	}

	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

	private String createAccount(CustomerID customer, String accountName) {

		for (Account a : customers.get(customer.getKey()).accounts) {
			if (a.getAccountName() == accountName) {
				return "Account already Exists";
			}
		}
		customers.get(customer.getKey()).addAccount(new Account(accountName, 0.0));
		return "Account: " + accountName + "Is created successfully";

	}

	private String move(CustomerID customer){
		String resp;
		
		newBankClientHandler.printOut("Enter amount");
		String amount = newBankClientHandler.getInput();
		newBankClientHandler.printOut("Enter sender type");
		String sender = newBankClientHandler.getInput();
		newBankClientHandler.printOut("Enter reciever type");
		String receiver = newBankClientHandler.getInput();
		newBankClientHandler.printOut("Making the movement...");
		Account senderAcc = customers.get(customer.getKey()).getAccount(sender);
		Account receiverAcc = customers.get(customer.getKey()).getAccount(receiver);

		senderAcc.withdraw(Double.parseDouble(amount));
		receiverAcc.deposit(Double.parseDouble(amount));
		resp="Transaction completed succefully";

		return resp;
	}

}
