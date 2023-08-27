package restaurant.dao;

import category.dto.CategoryDTO;
import exception.FindException;
import jdbc.JDBC;
import menu.dto.MenuDTO;
import region.dto.RegionDTO;
import restaurant.dto.RestaurantDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

public class RestaurantDAO {
    private int restaurantCount;
    public int getRestaurantCount() throws FindException {
        return restaurantCount;
    }
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
                    " FROM (SELECT res.restaurant_id, res.restaurant_name, NVL(res.rating_score, -1), reg.city_name, reg.si_gun_gu, LISTAGG(c.category_name, ',')" +
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
            String sql = "SELECT *" +
                         "FROM (SELECT res.restaurant_id, res.restaurant_name, res.view_count, res.run_time, res.detail_address, res.zipcode, reg.city_name, reg.si_gun_gu, reg.dong_eup_myeon, LISTAGG(c.category_name, ', '), res.rating_score" +
                         "FROM restaurants res" +
                         "JOIN regions reg ON res.zipcode = reg.zipcode" +
                         "JOIN restaurants_categories rc ON res.restaurant_id = rc.restaurant_id" +
                         "JOIN categories c ON c.category_id = rc.category_id" +
                         "AND EXISTS (SELECT 1" +
                         "FROM users u" +
                         "JOIN regions user_reg ON u.zipcode = user_reg.zipcode" +
                         "WHERE user_reg.city_name = reg.city_name" +
                         "AND u.user_id = 1" +
                         "AND user_reg.si_gun_gu = reg.si_gun_gu" +
                         ")" +
                         "GROUP BY res.restaurant_id, res.restaurant_name, res.view_count, res.run_time, res.detail_address, res.zipcode, reg.city_name, reg.si_gun_gu, reg.dong_eup_myeon, res.rating_score" +
                         "ORDER BY DBMS_RANDOM.RANDOM" +
                         ") WHERE ROWNUM = 1";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();
            RestaurantDTO rDTO = new RestaurantDTO();
            if (rs.next()) {
                rDTO.setId(rs.getInt(1));
                rDTO.setName(rs.getString(2));
                rDTO.setViewCount(rs.getInt(3));
                rDTO.setRatingScore(rs.getDouble(4));
                rDTO.setDetailAddress(rs.getString(5));
                rDTO.setRunTime(rs.getString(6));
                RegionDTO regDTO = new RegionDTO();
                regDTO.setZipcode(rs.getString(7));
                regDTO.setCityName(rs.getString(8));
                regDTO.setSiGunGu(rs.getString(9));
                regDTO.setDongEupMyeon(rs.getString(10));
                rDTO.setRegion(regDTO);

                ArrayList<CategoryDTO> categoriesList = new ArrayList<>();
                String[] categoryNames = rs.getString(11).split(",");
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
                rDTO.setRatingScore(rs.getDouble(12));

                sql = "SELECT m.*"+
                      "FROM menu m"+
                      "JOIN restaurants res ON res.restaurant_id = m.restaurant_id" +
                      "WHERE res.restaurant_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, rDTO.getId());
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
                rDTO.setMenu(menuList);
            }

//            ReviewDAO rDAO = new ReviewDAO();
//            rDTO.setReviews(rDAO.selectReviewByRestaurant(rDTO.getId(), 1));


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
}
