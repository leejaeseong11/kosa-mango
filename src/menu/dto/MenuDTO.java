package menu.dto;

import lombok.Data;

@Data
public class MenuDTO {
    private int id;
    private String fname;
    private int price;
    private String restaurantId;
}
