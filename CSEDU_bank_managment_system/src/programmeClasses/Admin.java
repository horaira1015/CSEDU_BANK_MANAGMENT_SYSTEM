package programmeClasses;

public class Admin extends Human {
	public Admin(String username, String password, String email) {
		super(username, password, email);
	}

	public Admin(String username, String fname, String lname, String phoneno, String email, String dob, String gender) {
		super(username, fname, lname, phoneno, email, dob, gender);
	}
	public Admin(String username, String password, String email, String fname, String lname,
				 String phoneno, String dob, String gender){
		super(username, password, email, fname, lname, phoneno, dob, gender);
	}
}
