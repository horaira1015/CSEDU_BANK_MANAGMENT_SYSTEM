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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class SavingAccountEnterViewControl implements Initializable {
	@FXML
	public TextField amount1;
	@FXML
	public TextField amount;
	@FXML
	public DatePicker ending;
	@FXML
	public Label interestRateShow;
	static SavingAccount x;

	public void goToAccountSelection(ActionEvent actionEvent) {
		new Bank().changeScene("CustomerAccountSelection.fxml", "Please Select Account", 720, 550);
	}

	public void deleteAc_go_to_selection(ActionEvent actionEvent) throws SQLException {
		float withdrawalAmount = 0;
		try {
			if (amount1.getText() == null || ending.getValue() == null) {
				Bank.showAlert(Alert.AlertType.ERROR, "Error", "Date is not selected Error", "Please select date");
				return;
			}
			withdrawalAmount = Float.parseFloat(amount1.getText());
		} catch (Exception e) {
			Bank.showAlert(Alert.AlertType.ERROR, "Error", "Date is not selected Error", "Please select date");
			return;
		}

		if (Bank.balance < withdrawalAmount) {
			Bank.showAlert(Alert.AlertType.ERROR, "Balance Error", "Sorry!! Lack of money", "Please wait");
			return;
		}
		float currentbal=CustomerLoginController.customer.balance;

		x.setEndDate(ending.getValue().toString());

		float totalBalance = x.calculateBalanceToDate(ending.getValue().toString());
		float updatedBalance = CustomerLoginController.customer.balance - x.balance + (withdrawalAmount - x.balance) ;
		float presentAcBalOriginal = x.balance;
		x.balance = Bank.balance;
		Withdraw withdrawTransaction = new Withdraw(CustomerLoginController.customer, x, totalBalance);
		withdrawTransaction.execute();

		deleteAccount(x.acnumber);

		Bank.balance -= withdrawalAmount;
		String query = "UPDATE customerall SET `Balance` = ? WHERE `username` = ?";
		try (PreparedStatement pstmt = Bank.con.prepareStatement(query)) {
			pstmt.setFloat(1, updatedBalance);
			pstmt.setString(2, CustomerLoginController.customername);
			pstmt.executeUpdate();
		}
		Bank.updateBalance(Bank.balance);

		CustomerLoginController.customer.balance = currentbal - presentAcBalOriginal + (withdrawalAmount - presentAcBalOriginal) ;
		new Bank().changeScene("CustomerAccountSelection.fxml", "Please Select Account", 720, 550);
	}

	private void deleteAccount(int accountNumber) throws SQLException {
		try (Statement st = Bank.con.createStatement()) {
			st.executeUpdate("DELETE FROM useraccounts WHERE accountNumber = " + accountNumber);
			st.executeUpdate("DELETE FROM advance WHERE accountNumber = " + accountNumber);
		}
	}

	public void calculate(ActionEvent actionEvent) throws SQLException {
		//x = SavingAccountstartViewControl.downloadSavingAccount(CustomerAccountSelectionCon.acnumber);
		float total = x.calculateBalanceToDate(ending.getValue().toString());
		amount1.setText(String.valueOf(total));
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			x = SavingAccountstartViewControl.downloadSavingAccount(CustomerAccountSelectionCon.acnumber);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		amount.setText(String.valueOf(x.balance));
		interestRateShow.setText(String.valueOf(AdvanceFeature.SAVING_INTEREST_RATE *100) + "%");

	}
}
