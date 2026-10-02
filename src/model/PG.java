package model;

import java.math.BigDecimal;
import java.util.HashSet;

public class PG {
	
	private Integer pgId;
	private String name;
	private String location;
	private Integer ownerId;
	private BigDecimal monthlyRent;
	private Integer availableBeds;
	private Integer totalBeds;
	private String sharingType;
	private BigDecimal rating;
	private BigDecimal distance;
	private HashSet<String> facilities;
	
	public PG()
	{
		
	}
	
	
	public PG(Integer pgId, String name, String location, Integer ownerId, BigDecimal monthlyRent,
			Integer availableBeds, Integer totalBeds, String sharingType, BigDecimal rating, BigDecimal distance,
			HashSet<String> facilities) {
		super();
		this.pgId = pgId;
		this.name = name;
		this.location = location;
		this.ownerId = ownerId;
		this.monthlyRent = monthlyRent;
		this.availableBeds = availableBeds;
		this.totalBeds = totalBeds;
		this.sharingType = sharingType;
		this.rating = rating;
		this.distance = distance;
		this.facilities = facilities;
	}


	public Integer getPgId() {
		return pgId;
	}


	public void setPgId(Integer pgId) {
		this.pgId = pgId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public Integer getOwnerId() {
		return ownerId;
	}


	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}


	public BigDecimal getMonthlyRent() {
		return monthlyRent;
	}


	public void setMonthlyRent(BigDecimal monthlyRent) {
		this.monthlyRent = monthlyRent;
	}


	public Integer getAvailableBeds() {
		return availableBeds;
	}


	public void setAvailableBeds(Integer availableBeds) {
		this.availableBeds = availableBeds;
	}


	public Integer getTotalBeds() {
		return totalBeds;
	}


	public void setTotalBeds(Integer totalBeds) {
		this.totalBeds = totalBeds;
	}


	public String getSharingType() {
		return sharingType;
	}


	public void setSharingType(String sharingType) {
		this.sharingType = sharingType;
	}


	public BigDecimal getRating() {
		return rating;
	}


	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}


	public BigDecimal getDistance() {
		return distance;
	}


	public void setDistance(BigDecimal distance) {
		this.distance = distance;
	}


	public HashSet<String> getFacilities() {
		return facilities;
	}


	public void setFacilities(HashSet<String> facilities) {
		this.facilities = facilities;
	}
	
	
	
	
}
