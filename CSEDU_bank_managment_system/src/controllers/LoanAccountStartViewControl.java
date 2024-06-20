package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import programmeClasses.Customer;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoanAccountStartViewControl implements Initializable {
	public TextField amount1;
	public TextField amount;
	public DatePicker starting;
	public Label interestRateShow;

	private int getNextAccountNumber() throws SQLException {
		String selectQuery = "SELECT accountNumber FROM useraccounts ORDER BY accountNumber DESC LIMIT 1;";
		try (PreparedStatement pstmt = Bank.con.prepareStatement(selectQuery);
			 ResultSet rs = pstmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("accountNumber") + 1;
			} else {
				return 1; // If no accounts exist, start with 1
			}
		}
	}
	private void createLoanAccount(int acN, float balance) throws SQLException {
		if(amount1.getText() == null || starting.getValue() == null){
			Bank.showAlert(Alert.AlertType.ERROR, "Error", "Selection Error", "Please select date and money");
			return;
		}
		try {
			if(String.valueOf(amount1.getText()) == null || String.valueOf(starting.getValue()) == null){
				Bank.showAlert(Alert.AlertType.ERROR, "Error", "Selection Error", "Please select date and money");
				return;
			}
		}catch (Exception e){
			Bank.showAlert(Alert.AlertType.ERROR, "Error", "Selection Error", "Date or amount is not selected");
			return;
		}
		if(Bank.balance < Float.parseFloat(amount1.getText())){
			Bank.showAlert(Alert.AlertType.ERROR, "Balance issue", "Bank balance issue", "Not enough Money");
			return;
		}
		Customer customer = CustomerLoginController.customer;
		Loan newAccount = new Loan(acN, 0, customer, starting.getValue().toString());

		// Insert into useraccounts first
		String userAccountsQuery = "INSERT INTO useraccounts (username, accountNumber, balance) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = Bank.con.prepareStatement(userAccountsQuery)) {
			pstmt.setString(1, customer.getUsername());
			pstmt.setInt(2, acN);
			pstmt.setFloat(3, 0);
			pstmt.executeUpdate();
		}

		// Insert into advance
		String advanceQuery = "INSERT INTO advance (username, accountNumber, balance, accountType, startingDate) VALUES (?, ?, ?, 'loan', ?)";
		try (PreparedStatement pstmt = Bank.con.prepareStatement(advanceQuery)) {
			pstmt.setString(1, customer.getUsername());
			pstmt.setInt(2, acN);
			pstmt.setFloat(3, balance);
			pstmt.setString(4, starting.getValue().toString());
			pstmt.executeUpdate();
		}

		// Execute deposit transaction
		Withdraw withdrawTransaction = new Withdraw(customer, newAccount, balance);
		withdrawTransaction.account.balance = balance*2;
		withdrawTransaction.execute();
		Bank.balance -= balance;
	}
	public void makeAc_go_to_selection(ActionEvent actionEvent) throws SQLException {
		float balance = 0;
		try {
			balance = Float.parseFloat(amount1.getText());
		} catch (Exception e) {
			Bank.showAlert(Alert.AlertType.ERROR, "Error", "Money Error", "Input is wrong");
			return;
		}
		int acN = getNextAccountNumber();
		createLoanAccount(acN, balance);
		Bank.updateBalance(Bank.balance);
		new Bank().changeScene("CustomerAccountSelection.fxml", "Please Select Account", 720, 550);
	}

	public static Loan downloadLoanAccount(int acNumber) throws SQLException {
		String query = "SELECT * FROM advance WHERE accountNumber = ?";
		try (PreparedStatement pstmt = Bank.con.prepareStatement(query)) {
			pstmt.setInt(1, acNumber);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Customer customer = CustomerLoginController.downloadCustomer(rs.getString("username"));
				int accountNumber = rs.getInt("accountNumber");
				float balance = rs.getFloat("balance");
				String startDate = rs.getString("startingDate");
				return new Loan(accountNumber, balance, customer, startDate);
			} else {
				throw new SQLException("Account not found");
			}
		}
	}

	public void goToAccountSelection(ActionEvent actionEvent) {
		new Bank().changeScene("CustomerAccountSelection.fxml", "Please Select Account", 720, 550);
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		interestRateShow.setText(String.valueOf(AdvanceFeature.LOAN_INTEREST_RATE *100) +"%");
		System.out.println(Bank.balance);
		amount.setText(String.valueOf(Bank.balance));
	}
}
