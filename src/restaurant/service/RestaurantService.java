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

    /**
     * @return 현재 검색한 결과의 전체 개수
     */
    public int getRestaurantCount() {
        return allRestaurantCount;
    }

    /**
     * 유형에 따른 식당 리스트 출력 (식당 이름, 평점, 주소, 카테고리)
     * @param searchType GENERAL_SEARCH-전체 검색, RANK_CATEGORY-카테고리 별 순위, RANK_REGION-지역 별 순위
     * @param searchWord 전체 검색에서 사용할 키워드, 순위 출력의 경우 null
     * @param index 1부터 시작하는 페이지
     * @throws FindException
     */
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

    /**
     * 식당 하나의 상세 정보를 출력
     * @param viewType VIEW_DETAIL-식당 상세 정보, VIEW_RANDOM-집 근처 랜덤 식당
     * @param orderIndex 사용자가 선택한 식당 번호 (식당의 id = restaurantIdList[orderIndex-1])
     * @throws FindException
     */
    public void printDetailRestaurant(String viewType, int orderIndex) throws FindException {
        RestaurantDAO rDao = new RestaurantDAO();
        if (viewType.equals("VIEW_DETAIL")) {

        } else if(viewType.equals("VIEW_RANDOM")) {
            RestaurantDTO rDto = rDao.randomRestaurantNearMyHouse(restaurantIdList[orderIndex-1]);
        }

        // print
    }
}
