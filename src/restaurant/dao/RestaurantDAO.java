package restaurant.dao;

import category.dto.CategoryDTO;
import exception.FindException;
import jdbc.JDBC;
import region.dto.RegionDTO;
import restaurant.dto.RestaurantDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
                    " OR INSTR(reg.city_name || reg.si_gun_gu || reg.dong_eup_myeon, ?) > 0";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, word);
            pstmt.setString(2, word);
            pstmt.setString(3, word);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                this.restaurantCount = rs.getInt(1);
            }

            sql = "SELECT *" +
                    " FROM (SELECT ROWNUM rn, r2.*" +
                    " FROM (SELECT ROWNUM, r1.*" +
                    " FROM (SELECT res.restaurant_id, res.restaurant_name, NVL(res.rating_score, -1), reg.city_name, reg.si_gun_gu, c.category_name" +
                    " FROM restaurants res" +
                    " JOIN regions reg ON res.zipcode = reg.zipcode" +
                    " JOIN restaurants_categories rc ON res.restaurant_id = rc.restaurant_id" +
                    " JOIN categories c ON c.category_id = rc.category_id" +
                    " JOIN menu m ON m.restaurant_id = res.restaurant_id" +
                    " WHERE INSTR(c.category_name, ?) > 0" +
                    " OR INSTR(m.menu_name, ?) > 0" +
                    " OR INSTR(reg.city_name || reg.si_gun_gu || reg.dong_eup_myeon, ?) > 0" +
                    " GROUP BY res.restaurant_id, res.restaurant_name, res.rating_score, reg.city_name, reg.si_gun_gu, c.category_name, view_count" +
                    " ORDER BY rating_score desc, view_count desc, res.restaurant_id) r1) r2 )" +
                    " WHERE rn BETWEEN ? AND ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, word);
            pstmt.setString(2, word);
            pstmt.setString(3, word);
            pstmt.setInt(4, pageSize * (index - 1) + 1);
            pstmt.setInt(5, pageSize * index + 1);
            rs = pstmt.executeQuery();

            int beforeRestaurantId = -1;
            while (rs.next()) {
                int tempId = rs.getInt(3);

                if (beforeRestaurantId != tempId) {
                    RestaurantDTO returnedRestaurant = new RestaurantDTO();
                    RegionDTO r = new RegionDTO();
                    beforeRestaurantId = tempId;
                    returnedRestaurant.setId(beforeRestaurantId);
                    returnedRestaurant.setName(rs.getString(4));
                    double ratingScore = rs.getDouble(5);
                    returnedRestaurant.setRatingScore(Math.round(ratingScore*100)/100.0);
                    r.setCityName(rs.getString(6));
                    r.setSiGunGu(rs.getString(7));
                    returnedRestaurant.setRegion(r);
                    CategoryDTO c = new CategoryDTO();
                    c.setName(rs.getString(8));
                    ArrayList<CategoryDTO> categoriesList = new ArrayList<>();
                    categoriesList.add(c);
                    returnedRestaurant.setCategories(categoriesList);
                    returnedRestaurants.add(returnedRestaurant);
                } else {
                    CategoryDTO c = new CategoryDTO();
                    c.setName(rs.getString(8));
                    returnedRestaurants.get(returnedRestaurants.size() - 1).getCategories().add(c);
                }
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
}
