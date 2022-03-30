package newbank.server;

import java.util.ArrayList;

public class Customer {
	
	ArrayList<Account> accounts;
	
	public Customer() {
		accounts = new ArrayList<>();
	}
	
	public String accountsToString() {
		String s = "";
		for(Account a : accounts) {
			s += a.toString();
		}
		return s;
	}

	public void addAccount(Account account) {
		accounts.add(account);		
	}

	// retrieve account
	public Account getAccount(String accountName) {
		for (Account a : accounts) {
			if (a.getAccountName().equals(accountName)) {
				return a;
			}
		}
		return null;
	}
}
