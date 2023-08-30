package restaurant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import category.dto.CategoryDTO;
import exception.FindException;
import jdbc.JDBC;
import menu.dto.MenuDTO;
import region.dto.RegionDTO;
import restaurant.dto.RestaurantDTO;

public class RestaurantDAO {
    private int restaurantCount;

    /**
     * 검색 메서드 호출 후 해당 메서드를 호출해야 함
     * @return 전체 식당 개수 반환
     * @throws FindException
     */
    public int getRestaurantCount() throws FindException {
        return restaurantCount;
    }

    /**
     * 검색 키워드가 식당 이름, 지역, 카테고리, 메뉴에 포함되어 있는 식당 리스트 반환
     * @param word 검색 키워드
     * @param pageSize 출력될 식당 리스트의 수
     * @param index 출력될 페이지 인덱스
     * @return 식당 DTO
     * @throws FindException
     */
    public ArrayList<RestaurantDTO> searchRestaurants(String word, int pageSize, int index) throws FindException {
        ArrayList<RestaurantDTO> returnedRestaurants = new ArrayList<>();

        Connection conn = null;
        try {
            conn = JDBC.connect();
        } catch (Exception e) {
            throw new FindException("DB 연결에 실패했습니다.");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT COUNT(distinct res.restaurant_id)" +
                    " FROM restaurants res " +
                    " JOIN regions reg ON res.zipcode = reg.zipcode" +
                    " JOIN restaurants_categories rc ON res.restaurant_id = rc.restaurant_id" +
                    " JOIN categories c ON c.category_id = rc.category_id" +
                    " JOIN menu m ON m.restaurant_id = res.restaurant_id" +
                    " WHERE INSTR(c.category_name, ?) > 0" +
                    " OR INSTR(m.menu_name, ?) > 0" +
                    " OR INSTR(reg.city_name || reg.si_gun_gu || reg.dong_eup_myeon, ?) > 0" +
                    " OR INSTR(res.restaurant_name, ?) > 0";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, word);
            pstmt.setString(2, word);
            pstmt.setString(3, word);
            pstmt.setString(4, word);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                this.restaurantCount = rs.getInt(1);
            }

            sql = "SELECT *" +
                    " FROM (SELECT ROWNUM rn, r2.*" +
                    " FROM (SELECT ROWNUM, r1.*" +
                    " FROM (SELECT res.restaurant_id, res.restaurant_name, NVL(res.rating_score, -1.0), reg.city_name, reg.si_gun_gu, LISTAGG(c.category_name, ',')" +
                    " FROM restaurants res" +
                    " JOIN regions reg ON res.zipcode = reg.zipcode" +
                    " JOIN restaurants_categories rc ON res.restaurant_id = rc.restaurant_id" +
                    " JOIN categories c ON c.category_id = rc.category_id" +
                    " JOIN menu m ON m.restaurant_id = res.restaurant_id" +
                    " WHERE INSTR(c.category_name, ?) > 0" +
                    " OR INSTR(m.menu_name, ?) > 0" +
                    " OR INSTR(reg.city_name || reg.si_gun_gu || reg.dong_eup_myeon, ?) > 0" +
                    " OR INSTR(res.restaurant_name, ?) > 0" +
                    " GROUP BY res.restaurant_id, res.restaurant_name, res.rating_score, reg.city_name, reg.si_gun_gu, view_count" +
                    " ORDER BY rating_score desc, view_count desc, res.restaurant_id) r1) r2 )" +
                    " WHERE rn BETWEEN ? AND ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, word);
            pstmt.setString(2, word);
            pstmt.setString(3, word);
            pstmt.setString(4, word);
            pstmt.setInt(5, pageSize * (index - 1) + 1);
            pstmt.setInt(6, pageSize * index);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                RestaurantDTO returnedRestaurant = new RestaurantDTO();
                RegionDTO r = new RegionDTO();
                returnedRestaurant.setId(rs.getInt(3));
                returnedRestaurant.setName(rs.getString(4));
                double ratingScore = rs.getDouble(5);
                returnedRestaurant.setRatingScore(Math.round(ratingScore*100)/100.0);
                r.setCityName(rs.getString(6));
                r.setSiGunGu(rs.getString(7));
                returnedRestaurant.setRegion(r);

                ArrayList<CategoryDTO> categoriesList = new ArrayList<>();
                String[] categoryNames = rs.getString(8).split(",");
                HashSet<String> categoryNamesSet = new HashSet<>();

                for(int i = 0; i < categoryNames.length; i++) {
                    categoryNamesSet.add(categoryNames[i]);
                }

                for(int i = 0; i < categoryNamesSet.size(); i++) {
                    CategoryDTO c = new CategoryDTO();
                    c.setName(categoryNames[i]);
                    categoriesList.add(c);
                }
                returnedRestaurant.setCategories(categoriesList);
                returnedRestaurants.add(returnedRestaurant);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new FindException("검색에 실패했습니다.");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new FindException("DB 종료에 실패했습니다.");
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    throw new FindException("DB 종료에 실패했습니다.");
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new FindException("DB 종료에 실패했습니다.");
                }
            }
        }

        return returnedRestaurants;
    }

    /**
     * @param userId 유저의 id
     * @return 유저의 집 주소와 동일한 랜덤한 식당 DTO
     * @throws FindException
     */
    public RestaurantDTO randomRestaurantNearMyHouse(int userId) throws FindException {
        RestaurantDTO returnedRestaurant = new RestaurantDTO();

        Connection conn = null;
        try {
            conn = JDBC.connect();
        } catch (Exception e) {
            throw new FindException("DB 연결에 실패했습니다.");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = " SELECT *" +
                    " FROM (SELECT *"+
                    " FROM (SELECT res.restaurant_id, res.restaurant_name, res.view_count, res.run_time, res.detail_address, res.zipcode, reg.city_name, reg.si_gun_gu, reg .dong_eup_myeon, LISTAGG(c.category_name, ','), NVL(res.rating_score, -1)"+
                    " FROM restaurants res"+
                    " JOIN regions reg ON res.zipcode = reg.zipcode"+
                    " JOIN restaurants_categories rc ON res.restaurant_id = rc.restaurant_id"+
                    " JOIN categories c ON c.category_id = rc.category_id"+
                    " AND EXISTS (SELECT 1"+
                    " FROM users u"+
                    " JOIN regions user_reg ON u.zipcode = user_reg.zipcode"+
                    " WHERE user_reg.city_name = reg.city_name"+
                    " AND u.user_id = ?"+
                    " AND user_reg.si_gun_gu = reg.si_gun_gu)"+
                    " GROUP BY res.restaurant_id, res.restaurant_name, res.view_count, res.run_time, res.detail_address, res.zipcode, reg.city_name, reg.si_gun_gu, reg .dong_eup_myeon, res.rating_score"+
                    " ORDER BY DBMS_RANDOM.RANDOM)"+
                    " WHERE ROWNUM = 1) ran"+
                    " JOIN menu m ON ran.restaurant_id = m.restaurant_id";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                returnedRestaurant.setId(rs.getInt(1));
                returnedRestaurant.setName(rs.getString(2));
                returnedRestaurant.setViewCount(rs.getInt(3));
                returnedRestaurant.setDetailAddress(rs.getString(5));
                returnedRestaurant.setRunTime(rs.getString(4));
                RegionDTO regDTO = new RegionDTO();
                regDTO.setZipcode(rs.getString(6));
                regDTO.setCityName(rs.getString(7));
                regDTO.setSiGunGu(rs.getString(8));
                regDTO.setDongEupMyeon(rs.getString(9));
                returnedRestaurant.setRegion(regDTO);

                ArrayList<CategoryDTO> categoriesList = new ArrayList<>();
                String[] categoryNames = rs.getString(10).split(",");

                HashSet<String> categoryNamesSet = new HashSet<>();

                for(int i = 0; i < categoryNames.length; i++) {
                    categoryNamesSet.add(categoryNames[i]);
                }

                for(int i = 0; i < categoryNamesSet.size(); i++) {
                    CategoryDTO c = new CategoryDTO();
                    c.setName(categoryNames[i]);
                    categoriesList.add(c);
                }
                returnedRestaurant.setCategories(categoriesList);
                returnedRestaurant.setRatingScore(rs.getDouble(11));

                ArrayList<MenuDTO> menuList = new ArrayList<>();
                do {
                    MenuDTO mDTO = new MenuDTO();
                    mDTO.setId(rs.getInt(12));
                    mDTO.setName(rs.getString(13));
                    mDTO.setPrice(rs.getInt(14));
                    mDTO.setRestaurantId(rs.getInt(15));
                    menuList.add(mDTO);
                } while (rs.next());
                returnedRestaurant.setMenu(menuList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new FindException("검색에 실패했습니다.");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new FindException("DB 종료에 실패했습니다.");
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    throw new FindException("DB 종료에 실패했습니다.");
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new FindException("DB 종료에 실패했습니다.");
                }
            }
        }
        return returnedRestaurant;
    }

    public RestaurantDTO selectDetailRestaurantInfo(int restaurantId) throws FindException {
        RestaurantDTO restaurantDetail = null;

        Connection conn = null;
        try {
            conn = JDBC.connect();
        } catch (Exception e) {
            throw new FindException("DB 연결에 실패했습니다.");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = " SELECT *" +
                    " FROM (SELECT *"+
                    " FROM (SELECT res.restaurant_id, res.restaurant_name, NVL(res.rating_score, -1), res.view_count, res.run_time, res.zipcode, reg.city_name, reg.si_gun_gu, reg.dong_eup_myeon, res.detail_address, LISTAGG(c.category_name, ',') c_name, res.rating_score" +
                    " FROM restaurants res" +
                    " JOIN regions reg ON res.zipcode = reg.zipcode" +
                    " JOIN restaurants_categories rc ON res.restaurant_id = rc.restaurant_id" +
                    " JOIN categories c ON c.category_id = rc.category_id" +
                    " JOIN menu m ON m.restaurant_id = res.restaurant_id" +
                    " GROUP BY res.restaurant_id, res.restaurant_name, res.view_count, res.run_time, res.detail_address, res.zipcode, reg.city_name, reg.si_gun_gu, reg .dong_eup_myeon, res.rating_score)"+
                    " WHERE restaurant_id = ?)";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, restaurantId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                restaurantDetail = new RestaurantDTO();
                RegionDTO r = new RegionDTO();
                restaurantDetail.setId(rs.getInt("restaurant_id"));
                restaurantDetail.setName(rs.getString("restaurant_name"));
                restaurantDetail.setViewCount(rs.getInt("view_count"));
                restaurantDetail.setRunTime(rs.getString("run_time"));
                restaurantDetail.setDetailAddress(rs.getString("detail_address"));

                r.setCityName(rs.getString("city_name"));
                r.setSiGunGu(rs.getString("si_gun_gu"));
                r.setDongEupMyeon(rs.getString("dong_eup_myeon"));
                restaurantDetail.setRegion(r);

                ArrayList<CategoryDTO> categoriesList = new ArrayList<>();
                String[] categoryNames = rs.getString("c_name").split(",");

                HashSet<String> categoryNamesSet = new HashSet<>();

                for(int i = 0; i < categoryNames.length; i++) {
                    categoryNamesSet.add(categoryNames[i]);
                }

                for(int i = 0; i < categoryNamesSet.size(); i++) {
                    CategoryDTO c = new CategoryDTO();
                    c.setName(categoryNames[i]);
                    categoriesList.add(c);
                }
                restaurantDetail.setCategories(categoriesList);
                restaurantDetail.setRatingScore(rs.getDouble("rating_score"));

                sql = " SELECT m.*"+
                        " FROM menu m"+
                        " JOIN restaurants res ON res.restaurant_id = m.restaurant_id" +
                        " WHERE res.restaurant_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, restaurantDetail.getId());
                rs = pstmt.executeQuery();
                ArrayList<MenuDTO> menuList = new ArrayList<>();
                while (rs.next()) {
                    MenuDTO mDTO = new MenuDTO();
                    mDTO.setId(rs.getInt(1));
                    mDTO.setName(rs.getString(2));
                    mDTO.setPrice(rs.getInt(3));
                    mDTO.setRestaurantId(rs.getInt(4));
                    menuList.add(mDTO);
                }
                restaurantDetail.setMenu(menuList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new FindException("식당 상세 정보 조회에 실패했습니다.");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new FindException("DB 종료에 실패했습니다.");
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    throw new FindException("DB 종료에 실패했습니다.");
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new FindException("DB 종료에 실패했습니다.");
                }
            }
        }

        return restaurantDetail;
    }

    public ArrayList<RestaurantDTO> rankRestaurantsByCategory(int categoryId, int pageSize, int index) throws FindException {
        ArrayList<RestaurantDTO> restaurantList = new ArrayList<>();

        Connection conn = null;
        try {
            conn = JDBC.connect();
        } catch (Exception e) {
            throw new FindException("DB 연결에 실패했습니다.");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT COUNT(distinct res.restaurant_id)" +
                    " FROM restaurants res " +
                    " JOIN regions reg ON res.zipcode = reg.zipcode" +
                    " JOIN restaurants_categories rc ON res.restaurant_id = rc.restaurant_id" +
                    " JOIN categories c ON c.category_id = rc.category_id" +
                    " JOIN menu m ON m.restaurant_id = res.restaurant_id" +
                    " WHERE rc.category_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, categoryId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                this.restaurantCount = rs.getInt(1);
            }

            sql = "SELECT *" +
                    " FROM (SELECT ROWNUM rn, r2.*" +
                    " FROM (SELECT ROWNUM, r1.*" +
                    " FROM (SELECT res.restaurant_id, res.restaurant_name, NVL(res.rating_score, -1.0), reg.city_name, reg.si_gun_gu, reg.dong_eup_myeon, LISTAGG(c.category_name, ',') category_name, res.view_count, res.run_time, res.detail_address" +
                    " FROM restaurants res" +
                    " JOIN regions reg ON res.zipcode = reg.zipcode" +
                    " JOIN restaurants_categories rc ON res.restaurant_id = rc.restaurant_id" +
                    " JOIN categories c ON c.category_id = rc.category_id" +
                    " JOIN menu m ON m.restaurant_id = res.restaurant_id" +
                    " WHERE rc.category_id = ?" +
                    " GROUP BY res.restaurant_id, res.restaurant_name, res.rating_score, reg.city_name, reg.si_gun_gu, reg.dong_eup_myeon, view_count, res.view_count, res.run_time, res.detail_address" +
                    " ORDER BY rating_score desc, view_count desc, res.restaurant_id) r1) r2 )" +
                    " WHERE rn BETWEEN ? AND ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, categoryId);
            pstmt.setInt(2, pageSize * (index - 1) + 1);
            pstmt.setInt(3, pageSize * index);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                RestaurantDTO restaurantDTO = new RestaurantDTO();
                RegionDTO r = new RegionDTO();
                restaurantDTO.setId(rs.getInt("restaurant_id"));
                restaurantDTO.setName(rs.getString("restaurant_name"));
                double ratingScore = rs.getDouble(3);
                restaurantDTO.setRatingScore(Math.round(ratingScore * 100) / 100.0);
                restaurantDTO.setViewCount(rs.getInt("view_count"));
                restaurantDTO.setRunTime(rs.getString("run_time"));
                restaurantDTO.setDetailAddress(rs.getString("detail_address"));
                r.setCityName(rs.getString("city_name"));
                r.setSiGunGu(rs.getString("si_gun_gu"));
                r.setDongEupMyeon(rs.getString("dong_eup_myeon"));
                restaurantDTO.setRegion(r);

                ArrayList<CategoryDTO> categoriesList = new ArrayList<>();
                String[] categoryNames = rs.getString("category_name").split(",");
                HashSet<String> categoryNamesSet = new HashSet<>();

                for(int i = 0; i < categoryNames.length; i++) {
                    categoryNamesSet.add(categoryNames[i]);
                }

                for(int i = 0; i < categoryNamesSet.size(); i++) {
                    CategoryDTO c = new CategoryDTO();
                    c.setName(categoryNames[i]);
                    categoriesList.add(c);
                }
                restaurantDTO.setCategories(categoriesList);
                restaurantList.add(restaurantDTO);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new FindException("카테고리 내 식당 랭킹 조회에 실패했습니다.");
        }  finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new FindException("DB 종료에 실패했습니다.");
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    throw new FindException("DB 종료에 실패했습니다.");
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new FindException("DB 종료에 실패했습니다.");
                }
            }
        }


        return restaurantList;
    }

    public ArrayList<RestaurantDTO> rankRestaurantsByRegion(String city_name, String si_gun_gu, int pageSize, int index) throws FindException {
        ArrayList<RestaurantDTO> restaurantList = new ArrayList<>();

        Connection conn = null;
        try {
            conn = JDBC.connect();
        } catch (Exception e) {
            throw new FindException("DB 연결에 실패했습니다.");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT COUNT(distinct res.restaurant_id)" +
                    " FROM restaurants res " +
                    " JOIN regions reg ON res.zipcode = reg.zipcode" +
                    " JOIN restaurants_categories rc ON res.restaurant_id = rc.restaurant_id" +
                    " JOIN categories c ON c.category_id = rc.category_id" +
                    " JOIN menu m ON m.restaurant_id = res.restaurant_id" +
                    " WHERE reg.city_name = ? AND reg.si_gun_gu = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, city_name);
            pstmt.setString(2, si_gun_gu);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                this.restaurantCount = rs.getInt(1);
            }

            sql = "SELECT *" +
                    " FROM (SELECT ROWNUM rn, r2.*" +
                    " FROM (SELECT ROWNUM, r1.*" +
                    " FROM (SELECT res.restaurant_id, res.restaurant_name, NVL(res.rating_score, -1.0), reg.city_name, reg.si_gun_gu, LISTAGG(c.category_name, ',') category_name, res.view_count, res.run_time, res.detail_address, reg.dong_eup_myeon" +
                    " FROM restaurants res" +
                    " JOIN regions reg ON res.zipcode = reg.zipcode" +
                    " JOIN restaurants_categories rc ON res.restaurant_id = rc.restaurant_id" +
                    " JOIN categories c ON c.category_id = rc.category_id" +
                    " JOIN menu m ON m.restaurant_id = res.restaurant_id" +
                    " WHERE reg.city_name = ? AND reg.si_gun_gu = ?" +
                    " GROUP BY res.restaurant_id, res.restaurant_name, res.rating_score, reg.city_name, reg.si_gun_gu, res.view_count, res.run_time, res.detail_address, reg.dong_eup_myeon" +
                    " ORDER BY rating_score desc, view_count desc, res.restaurant_id) r1) r2 )" +
                    " WHERE rn BETWEEN ? AND ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, city_name);
            pstmt.setString(2, si_gun_gu);
            pstmt.setInt(3, pageSize * (index - 1) + 1);
            pstmt.setInt(4, pageSize * index);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                RestaurantDTO restaurantDTO = new RestaurantDTO();
                RegionDTO r = new RegionDTO();
                restaurantDTO.setId(rs.getInt("restaurant_id"));
                restaurantDTO.setName(rs.getString("restaurant_name"));
                double ratingScore = rs.getDouble(3);
                restaurantDTO.setRatingScore(Math.round(ratingScore * 100) / 100.0);
                restaurantDTO.setViewCount(rs.getInt("view_count"));
                restaurantDTO.setRunTime(rs.getString("run_time"));
                restaurantDTO.setDetailAddress(rs.getString("detail_address"));
                r.setCityName(rs.getString("city_name"));
                r.setSiGunGu(rs.getString("si_gun_gu"));
                r.setDongEupMyeon(rs.getString("dong_eup_myeon"));
                restaurantDTO.setRegion(r);

                ArrayList<CategoryDTO> categoriesList = new ArrayList<>();
                String[] categoryNames = rs.getString("category_name").split(",");
                HashSet<String> categoryNamesSet = new HashSet<>();

                for(int i = 0; i < categoryNames.length; i++) {
                    categoryNamesSet.add(categoryNames[i]);
                }

                for(String categoryName : categoryNamesSet) {
                    CategoryDTO c = new CategoryDTO();
                    c.setName(categoryName);
                    categoriesList.add(c);
                }
                restaurantDTO.setCategories(categoriesList);
                restaurantList.add(restaurantDTO);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new FindException("지역 내 식당 랭킹 조회에 실패했습니다.");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new FindException("DB 종료에 실패했습니다.");
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    throw new FindException("DB 종료에 실패했습니다.");
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new FindException("DB 종료에 실패했습니다.");
                }
            }
        }
        return restaurantList;
    }
}

