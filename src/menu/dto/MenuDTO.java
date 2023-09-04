package menu.dto;

import lombok.Data;

@Data
public class MenuDTO {
    private int id;
    private String name;
    private int price;
    private int restaurantId;
}
