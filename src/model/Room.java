package model;

public class Room {
	private Integer  roomId;
	private Integer pgId;
	private String sharingType;
	private Integer capacity;
	private Integer occupiedBeds;
	
	
	public Room(Integer roomId, Integer pgId, String sharingType, Integer capacity, Integer occupiedBeds) {
		super();
		this.roomId = roomId;
		this.pgId = pgId;
		this.sharingType = sharingType;
		this.capacity = capacity;
		this.occupiedBeds = occupiedBeds;
	}


	public Integer getRoomId() {
		return roomId;
	}


	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}


	public Integer getPgId() {
		return pgId;
	}


	public void setPgId(Integer pgId) {
		this.pgId = pgId;
	}


	public String getSharingType() {
		return sharingType;
	}


	public void setSharingType(String sharingType) {
		this.sharingType = sharingType;
	}


	public Integer getCapacity() {
		return capacity;
	}


	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}


	public Integer getOccupiedBeds() {
		return occupiedBeds;
	}


	public void setOccupiedBeds(Integer occupiedBeds) {
		this.occupiedBeds = occupiedBeds;
	}
	
	
	
	
	

}
