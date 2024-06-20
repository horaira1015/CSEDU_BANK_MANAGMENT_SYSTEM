package controllers;

import java.sql.SQLException;

public interface Transaction {
	void validate() throws SQLException, IllegalArgumentException;
	void execute() throws SQLException;
}
