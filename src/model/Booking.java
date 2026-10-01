package model;

import java.time.LocalDateTime;

public class Booking {
	
	private Integer bookingId;
	private Integer studentId;
	private Integer piId;
	private LocalDateTime bookingDate;
	private String status;
	
	
	public Booking(Integer bookingId, Integer studentId, Integer piId, LocalDateTime bookingDate, String status) {
		super();
		this.bookingId = bookingId;
		this.studentId = studentId;
		this.piId = piId;
		this.bookingDate = bookingDate;
		this.status = status;
	}


	public Integer getBookingId() {
		return bookingId;
	}


	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}


	public Integer getStudentId() {
		return studentId;
	}


	public void setStudentId(Integer studentId) {
		this.studentId = studentId;
	}


	public Integer getPiId() {
		return piId;
	}


	public void setPiId(Integer piId) {
		this.piId = piId;
	}


	public LocalDateTime getBookingDate() {
		return bookingDate;
	}


	public void setBookingDate(LocalDateTime bookingDate) {
		this.bookingDate = bookingDate;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	

}
