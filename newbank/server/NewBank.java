package newbank.server;

import java.util.HashMap;

public class NewBank {

	private static final NewBank bank = new NewBank();
	private HashMap<String, Customer> customers;

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
		if (customers.containsKey(customer.getKey())) {
			switch (request) {
				case "SHOWMYACCOUNTS":
					return showMyAccounts(customer);
				case "NEWBANK Savings":
					return createAccount(customer, "Savings");
				case "NEWBANK Checking":
					return createAccount(customer, "Checking");
				case "NEWBANK Main":
					return createAccount(customer, "Main");
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

}
