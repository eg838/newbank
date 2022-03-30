package newbank.server;

public class Account {
	
	private String accountName;
	private double openingBalance;
	private double currentBalance;


	public Account(String accountName, double openingBalance) {
		this.accountName = accountName;
		this.openingBalance = openingBalance;
	}
	
	public String toString() {
		return (accountName + ": " + openingBalance);
	}

	public String getAccountName(){
		return (accountName);
	}
}
