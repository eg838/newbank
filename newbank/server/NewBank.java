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
	public synchronized String processRequest(CustomerID customer, String request,
			NewBankClientHandler newBankClientHandler) {
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
				case "PAY":
					return pay(customer, newBankClientHandler);
				case "MOVE":
					return move(customer, newBankClientHandler);
				case "Delete Savings":
					return deleteAccount(customer, "Savings");
				case "Delete Checking":
					return deleteAccount(customer, "Checking");
				case "Delete Main":
					return deleteAccount(customer, "Main");
				case "Change Password":
					return changePassword(customer, newBankClientHandler);
				case "Log Out":
					return logOut(newBankClientHandler);
			}
		}
		return "Incorrect Command Entered";
	}

	private String showMyAccounts(CustomerID customer) {
		return (customers.get(customer.getKey())).accountsToString();
	}

	private String deleteAccount(CustomerID customer, String accountName) {
		for (Account a : customers.get(customer.getKey()).accounts) {
			if (a.getAccountName() == accountName) {
				if (a.getCurrentBalance() > 0) {
					return "Unable to delete account whilst there is funds in the account, please move any funds to a different account prior to trying again.";
				} else {
					Customer currentCustomer = customers.get(customer.getKey());
					Account deletedAccount = currentCustomer.getAccount(accountName);
					currentCustomer.removeAccount(deletedAccount);
					return "Account Removed";
				}
			}
		}
		return "Account does not exist";
	}

	private String changePassword(CustomerID customer, NewBankClientHandler newBankClientHandler){
		newBankClientHandler.printOut("Please enter current password");
		String receiveCurrentPassword = newBankClientHandler.getInput();
		if(customers.get(customer.getKey()).password.equals(receiveCurrentPassword)){
			newBankClientHandler.printOut("Please enter new password of at least 8 characters");
			String receiveNewPasswordOne = newBankClientHandler.getInput();
			if(receiveNewPasswordOne.length() < 8){
				return "Password is not at least 8 characters long - Password Change Failed";
			}
			else {
				newBankClientHandler.printOut("Please enter new password again");
				String receiveNewPasswordTwo = newBankClientHandler.getInput();
				if (receiveNewPasswordOne.equals(receiveNewPasswordTwo)) {
					customers.get(customer.getKey()).registerPW(receiveNewPasswordOne);
					return "Password Updated";
				} else {
					return "Passwords Do Not Match";
				}
			}
		}
		else{return "Incorrect Password Entered";}
	}

	private String logOut(NewBankClientHandler newBankClientHandler) {
		System.out.println("Logged Out Successfully");
		newBankClientHandler.run();
		return "";
	}

	private String createAccount(CustomerID customer, String accountName) {
		for (Account a : customers.get(customer.getKey()).accounts) {
			if (a.getAccountName() == accountName) {
				return "Account Already Exists";
			}
		}
		customers.get(customer.getKey()).addAccount(new Account(accountName, 0.0));
		return "Account: " + accountName + " Created Successfully";

	}

	private String pay(CustomerID currentCustomer, NewBankClientHandler newBankClientHandler) {
		// STEP1 - receiver username
		newBankClientHandler.printOut("Enter username you would like to send money");
		String receiveCustName = newBankClientHandler.getInput();
		if (!customers.containsKey(receiveCustName)) {
			return "FAILED - The user does not exist";
		}

		// STEP2 - receiver account
		newBankClientHandler.printOut("Enter the receivers account name");
		String receiveAccount = newBankClientHandler.getInput();
		Account receiverAccount = customers.get(receiveCustName).getAccount(receiveAccount);
		if (receiverAccount == null) {
			return "FAILED - The account does not exist";
		}

		// STEP3 - sender account
		newBankClientHandler.printOut("Enter your account name to send money");
		String payAccountName = newBankClientHandler.getInput();
		Account senderAccount = customers.get(currentCustomer.getKey()).getAccount(payAccountName);
		if (senderAccount == null) {
			return "FAILED - The account does not exist";
		}

		// STEP4 - the amount of money to send
		newBankClientHandler.printOut("Enter the amount to send");
		String amount = newBankClientHandler.getInput();
		double numAmount = Integer.parseInt(amount);
		if (senderAccount.getCurrentBalance() < numAmount) {
			return "FAILED - Insufficient balance";
		}

		senderAccount.withdraw(numAmount);
		receiverAccount.deposit(numAmount);
		return "DEFAULT - SUCCESS";
	}

	private String move(CustomerID customer, NewBankClientHandler newBankClientHandler) {
		String resp;

		newBankClientHandler.printOut("Enter amount");
		String amount = newBankClientHandler.getInput();
		newBankClientHandler.printOut("Enter sender type");
		String sender = newBankClientHandler.getInput();
		newBankClientHandler.printOut("Enter receiver type");
		String receiver = newBankClientHandler.getInput();
		newBankClientHandler.printOut("Making the movement...");
		Account senderAcc = customers.get(customer.getKey()).getAccount(sender);
		Account receiverAcc = customers.get(customer.getKey()).getAccount(receiver);

		senderAcc.withdraw(Double.parseDouble(amount));
		receiverAcc.deposit(Double.parseDouble(amount));
		resp = "Transaction completed successfully";

		return resp;
	}
}
