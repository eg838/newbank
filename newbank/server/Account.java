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

	public String getAccountName(){
		return (accountName);
	}

	public double getOpeningBalance() {
		return openingBalance;
	}


	public double getCurrentBalance() {
		return currentBalance;
	}


	public void withdraw(double amount) {
		currentBalance = currentBalance - amount;
	}
	
	public void deposit(double amount) {
		currentBalance = currentBalance + amount;
	}
}
