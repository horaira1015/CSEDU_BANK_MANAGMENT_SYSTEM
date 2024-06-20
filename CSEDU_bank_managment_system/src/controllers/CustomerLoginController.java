package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import programmeClasses.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerLoginController {
	static Customer customer;
	static String customername;
	@FXML
	TextField username;
	@FXML
	TextField password;

	public void back() {
		new Bank().changeScene("openingPage.fxml","Start Screen",720, 550);
	}

	static public Customer downloadCustomer(String username) throws SQLException {
		String query2 = "Select * from customerAll where username='" + username + "';";
		Statement st = Bank.con.createStatement();
		st.executeQuery(query2);
		ResultSet rs = st.executeQuery(query2);
		rs.next();
		username = rs.getString(1);
		String fname = rs.getString(2);
		String lname = rs.getString(3);
		String phoneno = rs.getString(4);
		String email = rs.getString(5);
		String dob = rs.getString(6);
		String gender = rs.getString(7);
		Float balance = rs.getFloat(8);
		Customer c = new Customer(username, fname, lname, phoneno, email, dob, gender, balance);
		return c;
	}
	public static void uploadCustomer(Customer c) throws SQLException {
		String query1 = " INSERT INTO customerAll VALUES ('" + c.getUsername() + "','" + c.getFname() + "','" + c.getLname() + "','" + c.getPhoneno() + "','" + c.getEmail() + "','" + c.getDob() + "','" + c.getGender() + "' ," + 0 + " );";
		PreparedStatement ps = Bank.con.prepareStatement(query1);
		int i = ps.executeUpdate();
		if (i == 1) System.out.println("Success1");
		else System.out.println("Failure1");
		String query2 = " INSERT INTO customer VALUES ('" + c.getUsername() + "','" + c.getPassword() + "');";
		ps = Bank.con.prepareStatement(query2);
		int j = ps.executeUpdate();
		if (j == 1) System.out.println("Success2");
		else System.out.println("Failure2");
	}
	public void signedInCustomer(ActionEvent actionEvent) {
		try {
			String query1 = "Select username,password from customer where username='" + username.getText() + "';";
			Statement st = Bank.con.createStatement();
			ResultSet rs = st.executeQuery(query1);
			rs.next();
			String dbpassword = rs.getString(2);
			if (dbpassword.contentEquals(password.getText())) {
				customername = username.getText();
				customer = downloadCustomer(username.getText());
				new Bank().changeScene("CustomerAccountSelection.fxml", "Please Select Account", 720, 550);
			}
			else{
				Bank.showAlert(Alert.AlertType.ERROR, "Error", "Wrong Information", "Please check username or passwords");
			}
		} catch (SQLException sqlException) {
			System.out.println(sqlException);
			Bank.showAlert(Alert.AlertType.ERROR, "Error", "Wrong Information", "Please check username or passwords");
		}
	}

	public void registerCustomer(ActionEvent actionEvent) {
		new Bank().changeScene("register.fxml", "Customer - Registration", 720, 550);
	}
}
