package controllers;

import programmeClasses.Customer;

import java.sql.SQLException;
import java.sql.Statement;

public class Deposit implements Transaction {
	public Customer customer;
	public Account account;
	public float amount;

	public Deposit(Customer customer, Account account, float amount) {
		this.customer = customer;
		this.account = account;
		this.amount = amount;
	}

	@Override
	public void validate() throws IllegalArgumentException {
		if (amount <= 0) {
			throw new IllegalArgumentException("Deposit amount must be positive");
		}
	}

	@Override
	public void execute() throws SQLException {
		validate();
		account.depo(amount);
		String query = "UPDATE customerall set balance = "+customer.balance+" WHERE username = '" +customer.getUsername()+"'";
		Statement stmt = Bank.con.createStatement();
		stmt.executeUpdate(query);
		query = "UPDATE useraccounts SET balance = " + account.balance + " WHERE accountNumber = " + account.acnumber;
		stmt = Bank.con.createStatement();
		stmt.executeUpdate(query);
		query = "INSERT INTO transactions (AccountNumber, CustomerName, Transactiontype, Amount, ToAccountNumber) " +
				"VALUES (" + account.acnumber + ", '" + customer.getUsername() + "', 'deposit', " + amount + ", " + account.acnumber + ")";

		stmt = Bank.con.createStatement();
		stmt.executeUpdate(query);
		System.out.println(customer.balance);
		System.out.println(account.balance);
	}
}
