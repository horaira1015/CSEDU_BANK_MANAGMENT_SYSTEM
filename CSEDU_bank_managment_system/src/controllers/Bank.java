package controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;

public class Bank extends Application {
	public static Connection con;
	static Stage pstage;
	static float balance;

	public static void main(String[] args) throws SQLException {
		connectDB("jdbc:mysql://localhost:3306/CSEDU_BANK", "root", "12345678"); //connectDB(url,user,password);
		launch(args);
	}

	public static void connectDB(String url, String user, String pass) throws SQLException { //Connecting to database at the start of the program
		con = DriverManager.getConnection(url, user, pass);
		System.out.println(con);
	}

	public void changeScene(String gui, String title, int width, int height) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fxmlFiles/" + gui));//loading FXML file
			Scene scene = new Scene(root, width, height);
			pstage.setScene(scene);
			pstage.setTitle(title);
			pstage.setResizable(false);
			pstage.show();
		} catch (Exception e) {
			e.printStackTrace();  //Printing Exception
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		pstage = primaryStage;
		changeScene("openingPage.fxml", "Start Screen", 720, 550);
		balance = readBalanceFromFile("/txtFiles/bBalance.txt");
		System.out.println(balance);
	}

	public static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}

	private static float readBalanceFromFile(String resourcePath) {

		float fileBalance = 0.0f;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				Bank.class.getResourceAsStream(resourcePath)))) {
			String line = br.readLine();
			if (line != null) {
				fileBalance = Float.parseFloat(line);
			}
		} catch (IOException | NumberFormatException e) {
			e.printStackTrace();
		}
		return fileBalance;
	}

	private static void writeBalanceToFile(float balance, String resourcePath) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(resourcePath)))) {
			bw.write(Float.toString(balance));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void updateBalance(float newBalance) {
		balance = newBalance;
		writeBalanceToFile(balance, "src/txtFiles/bBalance.txt");
	}
}