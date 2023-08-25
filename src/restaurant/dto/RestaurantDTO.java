package restaurant.dto;

import category.dto.CategoryDTO;
import menu.dto.MenuDTO;

import java.util.ArrayList;

public class RestaurantDTO {
    private int id;
    private String name;
    private int viewCount;
    private String runTime;
    private String detailAddress;
    private ArrayList<ReviewDTO> reviews;
    private RegionDTO regions;
    private ArrayList<CategoryDTO> category;
    private ArrayList<MenuDTO> menu;

}
