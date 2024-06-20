package controllers;

import programmeClasses.Customer;

import java.sql.SQLException;
import java.sql.Statement;

public class Withdraw implements Transaction {
	private Customer customer;
	Account account;
	private float amount;

	public Withdraw(Customer customer, Account account, float amount) {
		this.customer = customer;
		this.account = account;
		this.amount = amount;
	}

	@Override
	public void validate() throws SQLException, IllegalArgumentException {
		if (amount <= 0) {
			throw new IllegalArgumentException("Withdrawal amount must be positive");
		}
		if (amount > account.balance) {
			throw new IllegalArgumentException("Insufficient balance");
		}
	}

	@Override
	public void execute() throws SQLException {
		validate();
		account.withdraw(amount);
		String query = "UPDATE customerall SET balance = "+customer.balance+" WHERE username = '" +customer.getUsername()+"'";
		Statement stmt = Bank.con.createStatement();
		stmt.executeUpdate(query);
		String updateQuery = "UPDATE useraccounts SET balance = " + account.balance + " WHERE accountNumber = " + account.acnumber;
		stmt = Bank.con.createStatement();
		stmt.executeUpdate(updateQuery);
		query = "INSERT INTO transactions (AccountNumber, CustomerName, Transactiontype, Amount, ToAccountNumber) " +
				"VALUES (" + account.acnumber + ", '" + customer.getUsername() + "', 'withdraw', " + amount + ", " + account.acnumber + ")";

		stmt = Bank.con.createStatement();
		stmt.executeUpdate(query);
		System.out.println(customer.balance);
		System.out.println(account.balance);
	}
}
