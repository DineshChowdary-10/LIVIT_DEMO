package model;

import java.time.LocalDateTime;

public class Booking {
	
	private Integer bookingId;
	private Integer studentId;
	private Integer pgId;
	private LocalDateTime bookingDate;
	private String status;
	
	public Booking()
	{
		
	}
	
	public Booking(Integer bookingId, Integer studentId, Integer pgId, LocalDateTime bookingDate, String status) {
		super();
		this.bookingId = bookingId;
		this.studentId = studentId;
		this.pgId = pgId;
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


	public Integer getPgId() {
		return pgId;
	}


	public void setPgId(Integer pgId) {
		this.pgId = pgId;
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
