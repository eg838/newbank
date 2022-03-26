package newbank.server;

import java.util.ArrayList;

public class Customer {

	private ArrayList<Account> accounts;
	private String password;

	public Customer() {
		accounts = new ArrayList<>();
		password = null;
	}

	public String accountsToString() {
		String s = "";
		for (Account a : accounts) {
			s += a.toString();
		}
		return s;
	}

	public void addAccount(Account account) {
		accounts.add(account);
	}

	public void registerPW(String pw) {
		password = pw;
	}

	public String getPW() {
		return password;
	}
}
