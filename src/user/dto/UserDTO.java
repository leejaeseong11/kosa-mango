package user.dto;

import java.util.ArrayList;

import lombok.Data;
import region.dto.RegionDTO;
import restaurant.dto.RestaurantDTO;

@Data
public class UserDTO {
	private	int id;
	private	String userId;
	private	String password;
	private	String userName;
	private	int gender; //  1-남자, 2-여자
	private	int status; // 1-활동 중, 2-탈퇴
	private String zipcode;
	private ArrayList<RestaurantDTO> restaurants;
	private RegionDTO region;

}