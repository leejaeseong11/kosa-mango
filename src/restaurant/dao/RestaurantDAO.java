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
            String sql = "SELECT res.restaurant_id, res.restaurant_name, NVL(res.rating_score, -1), res.view_count, res.run_time, reg.city_name, reg.si_gun_gu, reg.dong_eup_myeon, res.detail_address, c.category_name, m.menu_id, m.menu_name, m.price" +
                    " FROM restaurants res" +
                    " JOIN regions reg ON res.zipcode = reg.zipcode" +
                    " JOIN restaurants_categories rc ON res.restaurant_id = rc.restaurant_id" +
                    " JOIN categories c ON c.category_id = rc.category_id" +
                    " JOIN menu m ON m.restaurant_id = res.restaurant_id" +
                    " WHERE res.restaurant_id = ?";

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
                restaurantDetail.setCategories(categoriesList);
                restaurantDetail.setRatingScore(rs.getDouble(11));

                sql = " SELECT m.*"+
                      " FROM menu m"+
                      " JOIN restaurants res ON res.restaurant_id = m.restaurant_id" +
                      " WHERE res.restaurant_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, restaurantDetail.getId());
                rs = pstmt.executeQuery();
                ArrayList<MenuDTO> menuList = new ArrayList<>();
                do {
                    MenuDTO mDTO = new MenuDTO();
                    mDTO.setId(rs.getInt(12));
                    mDTO.setName(rs.getString(13));
                    mDTO.setPrice(rs.getInt(14));
                    mDTO.setRestaurantId(rs.getInt(15));
                    menuList.add(mDTO);
                } while (rs.next());
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
    
    public ArrayList<RestaurantDTO> rankRestaurantsByCategory(String categoryId, int pageSize, int index) throws FindException {
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
            pstmt.setString(1, categoryId);
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
                    " WHERE rc.category_id = ?" +
                    " GROUP BY res.restaurant_id, res.restaurant_name, res.rating_score, reg.city_name, reg.si_gun_gu, view_count" +
                    " ORDER BY rating_score desc, view_count desc, res.restaurant_id) r1) r2 )" +
                    " WHERE rn BETWEEN ? AND ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, categoryId);
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
    

}
