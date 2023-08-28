package user.dto;

import java.util.ArrayList;

import lombok.Data;
import region.dto.RegionDTO;
import restaurant.dto.RestaurantDTO;

@Data
public class UserDTO {
	private	String id;
	private	int userId;
	private	String password;
	private	String userName;
	private	int gender;
	private	int status;
	private String zipcode;
	private ArrayList<RestaurantDTO> restaurants;
	private RegionDTO region;


	public UserDTO(String id, String password, String userName, int gender, String zipcode) {
		super();
		this.id = id;
		this.password = password;
		this.userName = userName;
		this.gender = gender;
		this.zipcode = zipcode;
	}
}