package programmeClasses;

public class Customer extends Human {

	public Float balance;

	public Customer(String username, String password, String email) {
		super(username,password,email);
	}

	public Customer(String username, String fname, String lname, String phoneno, String email, String dob, String gender, Float balance) {
		super(username, fname, lname, phoneno, email, dob, gender);
		this.balance = balance;
	}

	public Customer(String username, String password, String email, String fname, String lname,
					String phoneno, String dob, String gender) {
	super(username, password, email, fname, lname, phoneno, dob, gender);
		this.balance = 0f;
	}

	public Float getBalance() {
		return balance;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}


}
