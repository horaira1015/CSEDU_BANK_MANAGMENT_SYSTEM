package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class SavingAccountstartViewControl implements Initializable {
	@FXML
	public TextField amount;
	@FXML
	public Label interestRateShow;
	@FXML
	public DatePicker starting;

	public void goToAccountSelection(ActionEvent actionEvent) {
		new Bank().changeScene("CustomerAccountSelection.fxml", "Please Select Account", 720, 550);
	}

	public void makeAc_go_to_selection(ActionEvent actionEvent) throws SQLException {
		float balance = 0;
		try {
			balance = Float.parseFloat(amount.getText());
		} catch (Exception e) {
			Bank.showAlert(Alert.AlertType.ERROR, "Error", "Money Error", "Input is wrong");
			return;
		}
		int acN = getNextAccountNumber();
		createFixedDepositAccount(acN, balance);
		new Bank().changeScene("CustomerAccountSelection.fxml", "Please Select Account", 720, 550);
	}

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

	private void createFixedDepositAccount(int acN, float balance) throws SQLException {
		Customer customer = CustomerLoginController.customer;
		try {
			if(starting.getValue() == null){
				Bank.showAlert(Alert.AlertType.ERROR, "Error", "Date Error", "Date is wrong");
				return;
			}
		}catch (Exception e){
			Bank.showAlert(Alert.AlertType.ERROR, "Error", "Date Error", "Date is wrong");
			return;
		}
		SavingAccount newAccount = new SavingAccount(acN, 0, customer, starting.getValue().toString());

		// Insert into useraccounts first
		String userAccountsQuery = "INSERT INTO useraccounts (username, accountNumber, balance) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = Bank.con.prepareStatement(userAccountsQuery)) {
			pstmt.setString(1, customer.getUsername());
			pstmt.setInt(2, acN);
			pstmt.setFloat(3, 0);
			pstmt.executeUpdate();
		}

		// Insert into advance
		String advanceQuery = "INSERT INTO advance (username, accountNumber, balance, accountType, startingDate) VALUES (?, ?, ?, 'fixedDeposit', ?)";
		try (PreparedStatement pstmt = Bank.con.prepareStatement(advanceQuery)) {
			pstmt.setString(1, customer.getUsername());
			pstmt.setInt(2, acN);
			pstmt.setFloat(3, balance);
			pstmt.setString(4, starting.getValue().toString());
			pstmt.executeUpdate();
		}

		Deposit depositTransaction = new Deposit(customer, newAccount, balance);
		depositTransaction.execute();

		Bank.balance += balance;
		Bank.updateBalance(Bank.balance);
	}

	public static SavingAccount downloadSavingAccount(int acNumber) throws SQLException {
		String query = "SELECT * FROM advance WHERE accountNumber = ?";
		try (PreparedStatement pstmt = Bank.con.prepareStatement(query)) {
			pstmt.setInt(1, acNumber);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Customer customer = CustomerLoginController.downloadCustomer(rs.getString("username"));
				int accountNumber = rs.getInt("accountNumber");
				float balance = rs.getFloat("balance");
				String startDate = rs.getString("startingDate");
				return new SavingAccount(accountNumber, balance, customer, startDate);
			} else {
				throw new SQLException("Account not found");
			}
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		interestRateShow.setText(String.valueOf(AdvanceFeature.SAVING_INTEREST_RATE *100) + "%");
	}
}
