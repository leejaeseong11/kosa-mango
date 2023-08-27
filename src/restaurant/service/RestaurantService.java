package restaurant.service;

import exception.FindException;
import restaurant.dao.RestaurantDAO;
import restaurant.dto.RestaurantDTO;

import java.util.ArrayList;

public class RestaurantService {
    private int pageSize;
    private int[] restaurantIdList;
    private int allRestaurantCount;
    public RestaurantService(int pageSize) {
        this.pageSize = pageSize;
        restaurantIdList = new int[pageSize];
    }

    public int getRestaurantCount() {
        return allRestaurantCount;
    }

    public void printRestaurantList(String searchType, String searchWord, int index) throws FindException {
        RestaurantDAO rDao = new RestaurantDAO();
        ArrayList<RestaurantDTO> rDtoList = null;
            try {
                if (searchType.equals("GENERAL_SEARCH")) {
                    rDtoList = rDao.searchRestaurants(searchWord, this.pageSize, index);
                    allRestaurantCount = rDao.getRestaurantCount();
                } else if (searchType.equals("RANK_CATEGORY")) {

                } else if (searchType.equals("RANK_REGION")) {

                }
            } catch (FindException e) {
                e.printStackTrace();
                throw new FindException("검색에 실패했습니다.");
            }

        if (rDtoList != null) {
            for (int i = 0; i < rDtoList.size(); i++) {
                RestaurantDTO rDto = rDtoList.get(i);
                String simpleAddress = rDto.getRegion().getCityName() + " " + rDto.getRegion().getSiGunGu();
                String[] categoryNames = new String[rDto.getCategories().size()];
                for (int j = 0; j < categoryNames.length; j++) {
                    categoryNames[j] = rDto.getCategories().get(j).getName();
                }
                restaurantIdList[i] = rDto.getId();
                if (rDto.getRatingScore() == -1) {
                    System.out.println(String.format("%d. %s / - / %s / %s", i+1, rDto.getName(), simpleAddress, String.join(" ", categoryNames)));
                } else {
                    System.out.println(String.format("%d. %s / %.1f점 / %s / %s", i+1, rDto.getName(), rDto.getRatingScore(), simpleAddress, String.join(" ", categoryNames)));
                }
            }
        }
    }

    public void printDetailRestaurant(String viewType, int orderIndex) throws FindException {
        RestaurantDAO rDao = new RestaurantDAO();
        if (viewType.equals("VIEW_DETAIL")) {
//        RestaurantDTO rDto = rDao.selectDetailRestaurantInfo(restaurantIdList[orderIndex-1]);
        } else if(viewType.equals("VIEW_RANDOM")) {
            RestaurantDTO rDto = rDao.randomRestaurantNearMyHouse(restaurantIdList[orderIndex-1]);
        }

        // print
    }
}
