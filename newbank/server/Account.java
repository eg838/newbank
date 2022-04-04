package newbank.server;

public class Account {

	private String accountName;
	private double openingBalance;
	private double currentBalance;

	public Account(String accountName, double openingBalance) {
		this.accountName = accountName;
		this.openingBalance = openingBalance;
		this.currentBalance = openingBalance;
	}

	public String toString() {
		return (accountName + ": " + openingBalance);
	}

	// return opening balance
	public double getOpeningBalance() {
		return openingBalance;
	}

	// return current balance
	public double getCurrentBalance() {
		return currentBalance;
	}

	// withdraw funds from account
	public void withdraw(double amount) {
		currentBalance = currentBalance - amount;
	}

	// deposit funds to account
	public void deposit(double amount) {
		currentBalance = currentBalance + amount;
	}

	// return account name
	public String getAccountName() {
		return accountName;
	}
}
