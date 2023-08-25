package user.dto;

import lombok.Data;

@Data
public class UserDTO {
	private	String id;
	private	int userId;
	private	String password;
	private	String userName;
	private	int gender;
	private	int status;
	private String zipcode;
	
	
	
	public UserDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public UserDTO(String id, int userId, String password, String userName, int gender, int status, String zipcode) {
		super();
		this.id = id;
		this.userId = userId;
		this.password = password;
		this.userName = userName;
		this.gender = gender;
		this.status = status;
		this.zipcode = zipcode;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}	
	
	
}