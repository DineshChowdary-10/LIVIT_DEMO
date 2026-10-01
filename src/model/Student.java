package model;

public class Student {
	private Integer studentId;
	private String name;
	private String phoneNumber;
	private String password;
	
	
	public Student(Integer studentId, String name, String phoneNumber, String password) {
		super();
		this.studentId = studentId;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.password = password;
	}


	public Integer getStudentId() {
		return studentId;
	}


	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	

}
