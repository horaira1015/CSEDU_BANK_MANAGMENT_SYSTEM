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

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminPageController implements Initializable {
	@FXML
	public TableView tableAlltr;
	public TableView tableallcust;
	public Label bankbalance;
	public Label totalWithdraw;
	public Label totalDeposits;
	public Label totalTransfer;
	public Tab startAllTransactions;
	public Label customerBalance;
	@FXML
	TableView depoTable;

	public void refreshedAllTr() {
		try {
			String selectQuery = "SELECT CustomerName, AccountNumber,Transactiontype,Amount FROM transactions";
			PreparedStatement pstmt = Bank.con.prepareStatement(selectQuery);
			ResultSet rs = pstmt.executeQuery();

			tableAlltr.getColumns().clear(); // Clear existing columns

			// Create TableColumn instances for each column in the ResultSet
			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				final int j = i;
				TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1)); // Column index starts from 1
				col.prefWidthProperty().bind(tableAlltr.widthProperty().divide(4));
				col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
						new SimpleStringProperty(param.getValue().get(j).toString()));

				tableAlltr.getColumns().add(col);
			}
			int totalTransactions = 0;
			ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
			while (rs.next()) {
				totalTransactions++;
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					row.add(rs.getString(i));
				}
				data.add(row);
			}

			tableAlltr.setItems(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void refreshallcust() {
		try {
			String selectQuery = "SELECT  username, accountNumber,balance FROM useraccounts";
			PreparedStatement pstmt = Bank.con.prepareStatement(selectQuery);
			ResultSet rs = pstmt.executeQuery();
			int totalCusBalance = 0;
			tableallcust.getColumns().clear(); // Clear existing columns

			// Create TableColumn instances for each column in the ResultSet
			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				final int j = i;
				TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1)); // Column index starts from 1
				col.prefWidthProperty().bind(tableallcust.widthProperty().divide(3));
				col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
						new SimpleStringProperty(param.getValue().get(j).toString()));

				tableallcust.getColumns().add(col);
			}

			ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
			while (rs.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					row.add(rs.getString(i));
				}
				data.add(row);
				totalCusBalance+=Integer.parseInt(rs.getString(3));
			}
			customerBalance.setText(String.valueOf(totalCusBalance));
			tableallcust.setItems(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}


	public void refreshdepo() {
		try {
			String selectQuery = "SELECT  customername, accountNumber,amount FROM transactions where transactiontype = 'deposit';";
			PreparedStatement pstmt = Bank.con.prepareStatement(selectQuery);
			ResultSet rs = pstmt.executeQuery();

			depoTable.getColumns().clear(); // Clear existing columns
			int am =  0;
			// Create TableColumn instances for each column in the ResultSet
			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				final int j = i;
				TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1)); // Column index starts from 1
				col.prefWidthProperty().bind(depoTable.widthProperty().divide(3));
				col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
						new SimpleStringProperty(param.getValue().get(j).toString()));

				depoTable.getColumns().add(col);
			}

			ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
			while (rs.next()) {
				am++;
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					row.add(rs.getString(i));
				}
				data.add(row);
			}
			totalDeposits.setText(String.valueOf(am));
			depoTable.setItems(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@FXML
	TableView withdrawTable;

	public void refreshwithdraw() {

		try {
			String selectQuery = "SELECT  customername, accountNumber,amount FROM transactions where transactiontype = 'withdraw';";
			PreparedStatement pstmt = Bank.con.prepareStatement(selectQuery);
			ResultSet rs = pstmt.executeQuery();

			withdrawTable.getColumns().clear(); // Clear existing columns
			int amount = 0;
			// Create TableColumn instances for each column in the ResultSet
			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				final int j = i;
				TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1)); // Column index starts from 1
				col.prefWidthProperty().bind(withdrawTable.widthProperty().divide(3));
				col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
						new SimpleStringProperty(param.getValue().get(j).toString()));

				withdrawTable.getColumns().add(col);
			}

			ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
			while (rs.next()) {
				amount++;
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					row.add(rs.getString(i));
				}
				data.add(row);
			}

			withdrawTable.setItems(data);
			totalWithdraw.setText(String.valueOf(amount));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	TableView transferTable;

	public void refreshtransfer() {
		try {
			String selectQuery = "SELECT  accountNumber,toAccountNumber,amount FROM transactions where transactiontype = 'transfer';";
			PreparedStatement pstmt = Bank.con.prepareStatement(selectQuery);
			ResultSet rs = pstmt.executeQuery();

			transferTable.getColumns().clear(); // Clear existing columns
			int am = 0;
			// Create TableColumn instances for each column in the ResultSet
			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				final int j = i;
				TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1)); // Column index starts from 1
				col.prefWidthProperty().bind(transferTable.widthProperty().divide(3));
				col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
						new SimpleStringProperty(param.getValue().get(j).toString()));

				transferTable.getColumns().add(col);
			}

			ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
			while (rs.next()) {
				am++;
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					row.add(rs.getString(i));
				}
				data.add(row);
			}

			transferTable.setItems(data);
			totalTransfer.setText(String.valueOf(am));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	TextField customerName;
	@FXML
	TableView table1;
	@FXML
	TableView table2;
	public void searchCustomer(){
		try {
			String customername = customerName.getText();
			String selectQuery = "SELECT  accountNumber,balance FROM useraccounts where username  ='"+customername +"';";
			PreparedStatement pstmt = Bank.con.prepareStatement(selectQuery);
			ResultSet rs = pstmt.executeQuery();

			table1.getColumns().clear(); // Clear existing columns

			// Create TableColumn instances for each column in the ResultSet
			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				final int j = i;
				TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1)); // Column index starts from 1
				col.prefWidthProperty().bind(table1.widthProperty().divide(2));
				col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
						new SimpleStringProperty(param.getValue().get(j).toString()));

				table1.getColumns().add(col);
			}

			ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
			while (rs.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					row.add(rs.getString(i));
				}
				data.add(row);
			}

			table1.setItems(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			String customername = customerName.getText();
			String selectQuery = "SELECT  accountNumber, transactiontype,amount FROM transactions where customername  ='"+customername +"';";
			PreparedStatement pstmt = Bank.con.prepareStatement(selectQuery);
			ResultSet rs = pstmt.executeQuery();

			table2.getColumns().clear(); // Clear existing columns

			// Create TableColumn instances for each column in the ResultSet
			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				final int j = i;
				TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1)); // Column index starts from 1
				col.prefWidthProperty().bind(table2.widthProperty().divide(3));
				col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param ->
						new SimpleStringProperty(param.getValue().get(j).toString()));

				table2.getColumns().add(col);
			}

			ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
			while (rs.next()) {
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					row.add(rs.getString(i));
				}
				data.add(row);
			}

			table2.setItems(data);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	@FXML
	Label totalCurtomerSet;
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		refreshtransfer();
		refreshallcust();
		refreshdepo();
		refreshwithdraw();
		refreshedAllTr();
		String sql = "SELECT * FROM customer";
		int totalCount = 0;
		try (
				PreparedStatement preparedStatement = Bank.con.prepareStatement(sql);
				// Execute query and get result set
				ResultSet resultSet = preparedStatement.executeQuery()
		) {
			while (resultSet.next()) {
				totalCount++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		totalCurtomerSet.setText(String.valueOf(totalCount));
		bankbalance.setText(String.valueOf(Bank.balance));
	}

	public void backTomainMenu(ActionEvent actionEvent) {
		new Bank().changeScene("adminLogin.fxml", "Admin - Login", 720, 550);
	}

	public void gotohome(ActionEvent actionEvent) {
		new Bank().changeScene("adminPage.fxml", "Option Menu", 720, 550);
	}
}
