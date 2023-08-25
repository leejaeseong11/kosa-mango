package restaurant.dto;

import lombok.Data;
import category.dto.CategoryDTO;
import menu.dto.MenuDTO;
import region.dto.RegionDTO;
import review.dto.ReviewDTO;

import java.util.ArrayList;

@Data
public class RestaurantDTO {
    private int id;
    private String name;
    private double ratingScore;
    private int viewCount;
    private String runTime;
    private String detailAddress;
    private ArrayList<ReviewDTO> reviews;
    private RegionDTO region;
    private ArrayList<CategoryDTO> categories;
    private ArrayList<MenuDTO> menu;

}
