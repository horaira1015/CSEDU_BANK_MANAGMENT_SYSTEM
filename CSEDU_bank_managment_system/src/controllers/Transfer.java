package controllers;

import programmeClasses.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Transfer implements Transaction {
	private Customer customer;
	private Account account;
	private int payeeAccountNumber;
	private float amount;

	public Transfer(Customer customer, Account account, int payeeAccountNumber, float amount) {
		this.customer = customer;
		this.account = account;
		this.payeeAccountNumber = payeeAccountNumber;
		this.amount = amount;
	}

	@Override
	public void validate() throws SQLException, IllegalArgumentException {
		if (amount <= 0) {
			throw new IllegalArgumentException("Transfer amount must be positive");
		}
		if (amount > account.balance) {
			throw new IllegalArgumentException("Insufficient balance");
		}

		// Check if payee account is a saving account
		String query2 = "SELECT accountNumber FROM advance WHERE accountNumber = ?";
		try (PreparedStatement pstmt = Bank.con.prepareStatement(query2)) {
			pstmt.setInt(1, payeeAccountNumber);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				// If payee account is found in advance, don't throw an exception
				System.out.println("Payee account found in advance table");
				throw new IllegalArgumentException("It's an advance account");
			} else {
				// Payee account is not a saving account, continue with other validations
				String query = "SELECT COUNT(*) FROM useraccounts WHERE accountnumber = ?";
				try (PreparedStatement pstmt2 = Bank.con.prepareStatement(query)) {
					pstmt2.setInt(1, payeeAccountNumber);
					ResultSet rs2 = pstmt2.executeQuery();
					if (rs2.next() && rs2.getInt(1) == 0) {
						throw new IllegalArgumentException("Payee account not found");
					}
				}
			}
		}
	}

	@Override
	public void execute() throws SQLException {
		validate();

		String getPayeeName = "SELECT username FROM useraccounts WHERE accountnumber = ?";
		String payeeName;
		try (PreparedStatement pstmt = Bank.con.prepareStatement(getPayeeName)) {
			pstmt.setInt(1, payeeAccountNumber);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				payeeName = rs.getString(1);
			} else {
				throw new SQLException("Payee account not found");
			}
		}

		Account payeeAccount = CustomerAccountSelectionCon.downloadAccount(payeeAccountNumber);
		Customer payeeAccountCustomer = CustomerLoginController.downloadCustomer(payeeName);
		if(payeeAccountCustomer.getUsername().equals(customer.getUsername())){
			System.out.println("same customer");
			payeeAccount.balance+=amount;
			account.balance-=amount;
		}
		else{
			payeeAccount.depo(amount);
			account.withdraw(amount);
		}

		String query1 = "UPDATE useraccounts SET Balance = " + account.balance + " WHERE accountNumber = " + account.acnumber;
		Statement stmt = Bank.con.createStatement();
		stmt.executeUpdate(query1);
		String query2 = "UPDATE useraccounts SET Balance = " + payeeAccount.balance + " WHERE accountNumber = " + payeeAccountNumber;
		stmt = Bank.con.createStatement();
		stmt.executeUpdate(query2);
		String query3 = "UPDATE customerall SET Balance = " + customer.balance + " WHERE username = '" +customer.getUsername()+"'";
		stmt = Bank.con.createStatement();
		stmt.executeUpdate(query3);



		String query4 = "UPDATE customerall SET Balance = " + payeeAccount.customer.balance + " WHERE username = '" +payeeAccount.customer.getUsername()+"'";
		stmt = Bank.con.createStatement();
		stmt.executeUpdate(query4);
		String query = "INSERT INTO transactions (AccountNumber, CustomerName, Transactiontype, Amount, ToAccountNumber) " +
				"VALUES (" + account.acnumber + ", '" + customer.getUsername() + "', 'transfer', " + amount + ", " + payeeAccountNumber + ")";

		stmt = Bank.con.createStatement();
		stmt.executeUpdate(query);
		System.out.println(customer.balance);
		System.out.println(account.balance);
		System.out.println(payeeAccount.balance);
		System.out.println(payeeAccountCustomer.balance);
	}
}