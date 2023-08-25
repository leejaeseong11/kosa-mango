package review.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ReviewDTO {
	private int id;
	private String content;
	private int rating;
	private int totalRating;
	private Date writingTime;
	private int userId;
	private int restaurantId;
}
