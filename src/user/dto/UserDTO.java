package user.dto;

import lombok.Data;

@Data
public class UserDTO {
	String id;
	int userId;
	String password;
	String userName;
	int gerder;
	int status;
	String zipcode;	
}