package restaurant.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import exception.FindException;
import menu.dto.MenuDTO;
import region.dto.RegionDTO;
import restaurant.dao.RestaurantDAO;
import restaurant.dto.RestaurantDTO;


public class RestaurantService {
    private int pageSize;
    private int[] restaurantIdList;
    private int allRestaurantCount;

    final static private HashMap<String, Integer> rankCategoryList = new HashMap<>();
    final static private HashMap<String, String> rankRegionList = new HashMap<>();
	

    public RestaurantService() {
        rankCategoryList.put("한식", 1);
        rankCategoryList.put("일식", 2);
        rankCategoryList.put("중식", 3);
        rankCategoryList.put("양식", 4);
        rankCategoryList.put("분식", 5);
        rankCategoryList.put("술", 15);
        
        rankRegionList.put("서울시","영등포구");
        rankRegionList.put("서울시","마포구");
        rankRegionList.put("서울시","송파구");
    }
  
    public HashMap<String, Integer> getRankCategoryList() {
        return rankCategoryList;
    }
    public HashMap<String, String> getRankRegionList(){
    	return rankRegionList;
    }
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
                    rDtoList = rDao.rankRestaurantsByCategory(rankCategoryList.get(searchWord), this.pageSize, index);
                    allRestaurantCount = rDao.getRestaurantCount();
                } else if (searchType.equals("RANK_REGION")) {
                	String[] searchWordsArray = searchWord.split("\\s+");
                    List<String> searchWordsList = Arrays.asList(searchWordsArray);
                    
                   
                    String searchWord1 = searchWordsList.get(0);
                    String searchWord2 = searchWordsList.get(1);
                        
                    rDtoList = rDao.rankRestaurantsByRegion(rankRegionList.get(searchWord1), rankRegionList.get(searchWord2), this.pageSize, index);
                    allRestaurantCount = rDao.getRestaurantCount();
                   
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
        RestaurantDTO rDto = null;

        if (viewType.equals("VIEW_DETAIL")) {
            rDto = rDao.selectDetailRestaurantInfo(restaurantIdList[orderIndex-1]);
        } else if(viewType.equals("VIEW_RANDOM")) {
            rDto = rDao.randomRestaurantNearMyHouse(restaurantIdList[orderIndex-1]);
        }

        if (rDto != null) {
            System.out.println(String.format("식당 이름: %s", rDto.getName()));
            System.out.println(String.format("카테고리: %s", rDto.getCategories().stream().map(categoryDTO -> categoryDTO.getName()).collect(Collectors.joining(" / "))));
            System.out.println(String.format("평점: %f, 조회수: %d", rDto.getRatingScore(), rDto.getViewCount()));
            RegionDTO regionDTO = rDto.getRegion();
            System.out.println(String.format("주소: %s %s %s %s", regionDTO.getCityName(), regionDTO.getSiGunGu(), regionDTO.getDongEupMyeon(), rDto.getDetailAddress()));
            System.out.println(String.format("영업 시간: %s", rDto.getRunTime()));
            ArrayList<MenuDTO> menuList = rDto.getMenu();
            System.out.println("=".repeat(20) + " 메뉴 정보" + "=".repeat(20));
            for (int i = 0; i < menuList.size(); i++) {
                System.out.println(String.format("⦁ %s : %s", i+1, menuList.get(i).getName(), menuList.get(i).getPrice()));
            }
        }
    }
}
