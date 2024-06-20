package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import programmeClasses.Customer;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AccountTransaction implements Initializable {
	@FXML
	Label username;
	@FXML
	Label id;
	@FXML
	Label balance;
	@FXML
	TextField deposit;
	@FXML
	TextField withdraw;
	@FXML
	TextField fname;
	@FXML
	TextField lname;
	@FXML
	TextField phoneno;
	@FXML
	TextField email;
	@FXML
	DatePicker dob;
	@FXML
	ComboBox<String> gender;
	@FXML
	TextField tId;
	@FXML
	TextField tAmount;

	Customer c;
	Account a;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			c = CustomerLoginController.customer;
			a = CustomerAccountSelectionCon.account;
			username.setText(c.getUsername());
			id.setText(a.acnumber + "");
			balance.setText(a.balance + "");
			fname.setText(c.getFname());

			lname.setText(c.getLname());
			phoneno.setText(c.getPhoneno());
			email.setText(c.getEmail());
			dob.setValue(LocalDate.parse(c.getDob()));
			gender.setItems(FXCollections.observableArrayList("Male", "Female"));
			gender.setValue(c.getGender());
			deposit.setText(String.valueOf(0));
			withdraw.setText(String.valueOf(0));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Deposit
	public void dAdd(float rs) {
		deposit.setText(Float.toString(Float.parseFloat(deposit.getText()) + rs));
	}

	public void dAdd100() {
		dAdd(100);
	}

	public void dAdd500() {
		dAdd(500);
	}

	public void dAdd1000() {
		dAdd(1000);
	}

	public void dAdd2000() {
		dAdd(2000);
	}

	public void dAdd5000() {
		dAdd(5000);
	}

	public void dAdd10000() {
		dAdd(10000);
	}

	public void reset() {
		deposit.setText("0");
		withdraw.setText("0");
	}

	public void dProceed() throws SQLException {
		float depositMoney = 0;
		//System.out.println("BEfore " + c.balance +" " + a.balance);
		try {
			depositMoney = Float.parseFloat(deposit.getText());
		} catch (Exception e) {
			Bank.showAlert(Alert.AlertType.ERROR, "Error", "Money Error", "Input is wrong");
			return;
		}
		Deposit depositTransaction = new Deposit(c, a, depositMoney);
		//System.out.println("ami tonmoy na2");
		try {
			depositTransaction.execute();
			//updateBalance();
			//System.out.println("ami tonmoy na1");
			balance.setText(String.valueOf(a.balance));
			showAlert(Alert.AlertType.INFORMATION, "Information", "Deposit Transaction Completed", "Thank You");
		} catch (SQLException | IllegalArgumentException e) {
			showAlert(Alert.AlertType.ERROR, "Error", "Deposit Transaction Failed", e.getMessage());
		}
		//System.out.println("After " + c.balance +" " + a.balance);
	}

	//Withdraw
	public void wAdd(float rs) {
		withdraw.setText(Float.toString(Float.parseFloat(withdraw.getText()) + rs));
	}

	public void wAdd100() {
		wAdd(100);
	}

	public void wAdd500() {
		wAdd(500);
	}

	public void wAdd1000() {
		wAdd(1000);
	}

	public void wAdd2000() {
		wAdd(2000);
	}

	public void wAdd5000() {
		wAdd(5000);
	}

	public void wAdd10000() {
		wAdd(10000);
	}

	public void wProceed() throws SQLException {
		float withdrawMoney = 0;
		try {
			withdrawMoney = Float.parseFloat(withdraw.getText());
		} catch (Exception e) {
			Bank.showAlert(Alert.AlertType.ERROR, "Error", "Money Error", "Input is wrong");
			return;
		}
		Withdraw withdrawTransaction = new Withdraw(c, a, withdrawMoney);
		try {
			withdrawTransaction.execute();
			balance.setText(Float.toString(a.balance));
			showAlert(Alert.AlertType.INFORMATION, "Information", "Withdraw Transaction Completed", "Thank You");
		} catch (SQLException | IllegalArgumentException e) {
			showAlert(Alert.AlertType.ERROR, "Error", "Withdraw Transaction Failed", e.getMessage());
		}
	}

	//Money Transfer
	public void transferMoney() throws SQLException {
		int payeeAcNumber = 0;
		float amount = 0;
		try {
			payeeAcNumber = Integer.parseInt(tId.getText());
			amount = Float.parseFloat(tAmount.getText());
		} catch (Exception e) {
			Bank.showAlert(Alert.AlertType.ERROR, "Error", "Money Error", "Input is wrong");
			return;
		}

		Transfer transferTransaction = new Transfer(c, a, payeeAcNumber, amount);
		try {
			transferTransaction.execute();
			balance.setText(Float.toString(a.balance));
			showAlert(Alert.AlertType.INFORMATION, "Information", "Transfer Transaction Completed", "Thank You");
		} catch (SQLException | IllegalArgumentException e) {
			showAlert(Alert.AlertType.ERROR, "Error", "Transfer Transaction Failed", e.getMessage());
		}
	}

	//Change Details
	public void update() throws SQLException {
		String query = "UPDATE customerall SET `fname` = ?, `lname` = ?, `phoneno` = ?, `email` = ?, `dob` = ? WHERE `username` = ?";
		try (PreparedStatement pstmt = Bank.con.prepareStatement(query)) {
			pstmt.setString(1, fname.getText());
			pstmt.setString(2, lname.getText());
			pstmt.setString(3, phoneno.getText());
			pstmt.setString(4, email.getText());
			pstmt.setString(5, dob.getValue().toString());
			pstmt.setString(6, CustomerLoginController.customername);
			int i = pstmt.executeUpdate();
			if (i == 1) {
				System.out.println("Updated");
			} else {
				System.out.println("Not updated");
			}
		}
		showAlert(Alert.AlertType.INFORMATION, "Information", "Update Completed", "Thank You");
	}

	//Sign out
	public void signOut() {
		new Bank().changeScene("CustomerAccountSelection.fxml", "Select AC", 720, 550);
	}

	//Show alert
	public static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}
}
