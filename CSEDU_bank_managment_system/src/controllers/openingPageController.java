package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;

public class openingPageController {
	public void quitProgramme(ActionEvent actionEvent) {

		Bank.updateBalance(Bank.balance);
		Platform.exit();
	}

	public void userLogin(ActionEvent actionEvent) {
		new Bank().changeScene("CustomerLogin.fxml", "Customer - Login", 720, 550);
	}

	public void adminLogin(ActionEvent actionEvent) {
		new Bank().changeScene("adminLogin.fxml", "Admin - Login", 720, 550);
	}
}
