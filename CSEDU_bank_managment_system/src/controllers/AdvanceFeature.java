package controllers;

interface AdvanceFeature {
	// Constants for interest rates
	double LOAN_INTEREST_RATE = 0.05; // 5%
	double SAVING_INTEREST_RATE = 0.03; // 3%

	// Method to get start date
	String getStartDate();

	// Method to set end date
	void setEndDate(String endDate);

	// Method to calculate duration between start and end date
	long calculateDuration();

	// Method to calculate balance up to a specific date
	float calculateBalanceToDate(String toDate);
}