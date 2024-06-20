package programmeClasses;

abstract class Human {
	private String username;
	private String password;
	private String email;
	private String fname;
	private String lname;
	private String phoneno;
	private String dob;
	private String gender;

	//constructor
	public Human(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
	}


	public Human(String username, String fname, String lname, String phoneno, String email, String dob, String gender) {
		this.username = username;
		this.fname = fname;
		this.lname = lname;
		this.phoneno = phoneno;
		this.email = email;
		this.dob = dob;
		this.gender = gender;
	}

	public Human(String username, String password, String email, String fname, String lname, String phoneno, String dob, String gender) {
		this(username, password, email);
		this.fname = fname;
		this.lname = lname;
		this.phoneno = phoneno;
		this.dob = dob;
		this.gender = gender;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public String getFname() {
		return fname;
	}

	public String getLname() {
		return lname;
	}

	public String getPhoneno() {
		return phoneno;
	}

	public String getDob() {
		return dob;
	}

	public String getGender() {
		return gender;
	}


}
