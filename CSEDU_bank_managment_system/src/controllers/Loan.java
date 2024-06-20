package controllers;

import programmeClasses.Customer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

class Loan extends Account implements AdvanceFeature {
	private String startDate;
	private String endDate;
	//private float initialBalance;

	Loan(int acnumber, float balance, Customer customer, String startDate) {
		super(acnumber, balance, customer);
		this.startDate = startDate;
		this.endDate = null;
	}

	@Override
	public String getStartDate() {
		return startDate;
	}

	@Override
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public long calculateDuration() {
		LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		return ChronoUnit.DAYS.between(start, end);
	}

	@Override
	public float calculateBalanceToDate(String toDate) {
		LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate end = LocalDate.parse(toDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		// Calculate interest accrued
		long duration = ChronoUnit.DAYS.between(start, end);
		double dailyInterest = balance * LOAN_INTEREST_RATE / 365; // Assuming daily compounding
		float accruedInterest = (float) (dailyInterest * duration);

		return balance + accruedInterest;
	}

	// Additional methods specific to Loan
}
