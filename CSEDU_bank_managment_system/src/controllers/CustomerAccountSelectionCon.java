package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import programmeClasses.Customer;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class CustomerAccountSelectionCon implements Initializable {

	@FXML
	public Label TotalAccount;
	@FXML
	public Label Fullbalance;
	@FXML
	TableView tableView;
	@FXML
	Label username;
	@FXML
	TextField acNumber;
	static Customer c = CustomerLoginController.customer;
	static int acnumber;
	static Account account;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		int countAccounts = 0;
		float countBalance = 0;
		try {
			c = CustomerLoginController.customer;
			System.out.println(c);
			username.setText(c.getUsername());
			countAccounts = 0;
			String selectQuery = "SELECT accountNumber, balance FROM useraccounts where username='" + c.getUsername() + "';";
			PreparedStatement pstmt = Bank.con.prepareStatement(selectQuery);
			ResultSet rs = pstmt.executeQuery();
			tableView.getColumns().clear();

			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				final int j = i;
				TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1)); // Column index starts from 1
				col.prefWidthProperty().bind(tableView.widthProperty().divide(2));
				col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
						new SimpleStringProperty(param.getValue().get(j).toString()));

				tableView.getColumns().add(col);
			}

			ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
			while (rs.next()) {
				countAccounts++;
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					row.add(rs.getString(i));
				}
				data.add(row);
			}
			tableView.setItems(data);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		countBalance = c.getBalance();
		TotalAccount.setText(String.valueOf(countAccounts));
		Fullbalance.setText(String.valueOf(countBalance));
	}

	public static Account downloadAccount(int id) throws SQLException {

		String query2 = "Select * from useraccounts where accountNumber='" + id + "';";
		Statement st = Bank.con.createStatement();
		st.executeQuery(query2);
		ResultSet rs = st.executeQuery(query2);
		rs.next();
		String userName =  rs.getString(1);
		int accountNumber = rs.getInt(2);
		int balance = rs.getInt(3);
		Account c = new Account(accountNumber, balance, CustomerLoginController.downloadCustomer(userName));
		return c;
	}

	public void enterAccount(ActionEvent actionEvent) throws SQLException {
		acnumber = 0;
		try {
			acnumber = Integer.parseInt(acNumber.getText());
		} catch (Exception e) {
			Bank.showAlert(Alert.AlertType.ERROR, "Error", "Account selection Error", "Please insert correct account number");
			return;
		}
		int x = 0;
		try {
			String query2 = "Select accountNumber from advance where accountNumber='" + acnumber + "'AND accountType='fixedDeposit'and username ='" + c.getUsername()+ "';";
			Statement st = Bank.con.createStatement();
			st.executeQuery(query2);
			ResultSet rs = st.executeQuery(query2);
			rs.next();
			if (rs.getString(1) == null) {
				System.out.println("amiNull");
			}
			x = 1;
			new Bank().changeScene("savingAccountEnterView.fxml", "Saving Account", 720, 550);
		} catch (Exception e) {
			System.out.println("it's not a depo");
		}
		//System.out.println("ami dhuklam matro");
		if (x == 1) {
			return;
		}
		try {
			String query2 = "Select accountNumber from advance where accountNumber='" + acnumber + "'AND accountType='loan' and username ='" + c.getUsername() + "';";
			Statement st = Bank.con.createStatement();
			st.executeQuery(query2);
			ResultSet rs = st.executeQuery(query2);
			rs.next();
			if (rs.getString(1) == null) {
				System.out.println("amiNull");
			}
			x = 1;
			new Bank().changeScene("LoanAccountEnterView.fxml", "Loan Account", 720, 550);
		} catch (Exception e) {
			System.out.println("it's not a saving");
		}
		if (x == 1) {
			return;
		}
		float mybalance = 0;
		try {
			String query2 = "Select accountNumber, balance  from useraccounts where accountNumber='" + acnumber + "'AND username='" + c.getUsername() + "';";
			System.out.println(CustomerLoginController.customername);
			Statement st = Bank.con.createStatement();
			st.executeQuery(query2);
			ResultSet rs = st.executeQuery(query2);
			rs.next();
			mybalance = Float.parseFloat(rs.getString(2));
		} catch (Exception e) {
			Bank.showAlert(Alert.AlertType.ERROR, "OOPS!", "Selection Error", "Please select correct account number");
			return;

		}
		account = new Account(acnumber, mybalance, c);
		new Bank().changeScene("AccountTransactionHere.fxml", "accountPage", 720, 550);
	}

	public void signout(ActionEvent actionEvent) {
		new Bank().changeScene("CustomerLogin.fxml", "Customer - Login", 720, 550);
	}

	public void makeNewAccount(ActionEvent actionEvent) throws SQLException {
		Statement st = Bank.con.createStatement();
		String query = "INSERT INTO useraccounts (username, balance) VALUES ('" + c.getUsername() + "', 0)";
		int i = st.executeUpdate(query);
		if (i == 1) System.out.println("Updated");
		else System.out.println("Not updated");
		Bank.showAlert(Alert.AlertType.INFORMATION, "Information", "Update Completed", "Thank You");

		new Bank().changeScene("CustomerAccountSelection.fxml", "Please Select Account", 720, 550);
	}

	public void takeLoan(ActionEvent actionEvent) {
		new Bank().changeScene("LoanAccountStartView.fxml", "loan", 720, 550);

	}

	public void fixedDeposit(ActionEvent actionEvent) {
		new Bank().changeScene("savingAccountStartView.fxml", "fixedDeposit", 720, 550);
	}
}
