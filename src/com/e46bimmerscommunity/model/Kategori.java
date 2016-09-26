package com.e46bimmerscommunity.model;

public class Kategori {
	private int Id           = 0;
	private String UserId    = "";
	private String Name      = "";
	private String Latitude  = "";
	private String Longitude = "";

	// constructor tanpa parameter
    public Kategori() {
    
    }
    
    public Kategori(int Id, String Name, String Latitude, String Longitude){
        this.Id = Id;
        this.Name = Name;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }
    
    public Kategori(String UserId, String Name, String Latitude, String Longitude){
        this.UserId    = UserId;
        this.Name      = Name;
        this.Latitude  = Latitude;
        this.Longitude = Longitude;
    }
    
	// constructor dengan parameter
    public Kategori(String Name){
        this.Name = Name;
    }
	
	/**
	 * 
	 * @param Id
	 */
	public void setId(int Id) {
		this.Id = Id;
	}

	public int getId() {
		return Id;
	}
	
	/**
	 * 
	 * @param UserId
	 */
	public void setUserId(String UserId) {
		this.UserId = UserId;
	}

	public String getUserId() {
		return UserId;
	}
	
	/**
	 * 
	 * @param Name
	 */
	public void setName(String Name) {
		this.Name = Name;
	}

	public String getName() {
		return Name;
	}
	
	/**
	 * 
	 * @param Latitude
	 */
	public void setLatitude(String Latitude) {
		this.Latitude = Latitude;
	}

	public String getLatitude() {
		return Latitude;
	}
	
	/**
	 * 
	 * @param Longitude
	 */
	public void setLongitude(String Longitude) {
		this.Longitude = Longitude;
	}

	public String getLongitude() {
		return Longitude;
	}
	
}