package newbank.server;

public class Account {

	private String accountName;
	private double currentBalance;

	public Account(String accountName, double currentBalance) {
		this.accountName = accountName;
		this.currentBalance = currentBalance;
	}

	public String toString() {
		return (accountName + ": " + currentBalance);
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
