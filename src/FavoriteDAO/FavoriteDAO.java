package FavoriteDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import jdbc.JDBC;
import restaurant.dto.RestaurantDTO;

public class FavoriteDAO {

	public static void main(String[] args) {
		FavoriteDAO fa = new FavoriteDAO();

		//식당 찜 등록
		//fa.insertFavorites(1, 3);

		//찜 목록 출력
		ArrayList<RestaurantDTO> list = fa.selectFavoritesByUserId("danbi52600");



	}

	// 회원아이디 찜 식당 조회
	public ArrayList<RestaurantDTO> selectFavoritesByUserId(String id) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		ArrayList<RestaurantDTO> list = new ArrayList<RestaurantDTO>();

		try {
			conn = JDBC.connect();
			System.out.println("connect");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String selectSQL = "SELECT r.RESTAURANT_NAME, r.VIEW_COUNT, r.DETAIL_ADDRESS, r.RUN_TIME FROM FAVORITES f JOIN USERS u ON f.USER_ID = u.USER_ID JOIN RESTAURANTS r ON f.RESTAURANT_ID = r.RESTAURANT_ID "
						+ "WHERE U.id = ?";

		try {
			pstmt = conn.prepareStatement(selectSQL);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				//식당 정보 저장
				RestaurantDTO restaurant = new RestaurantDTO();
				restaurant.setName(rs.getString(1));
				restaurant.setViewCount(rs.getInt(2));
				restaurant.setDetailAddress(rs.getString(3));
				restaurant.setRunTime(rs.getString(4));

				list.add(restaurant);	//검색 결과 리스트 추가
			}

			for(RestaurantDTO r : list) {
				System.out.println(r);	//형식에 맞는 toString 구현할것
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block

				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}

		}

		return list;

	}

	// 찜 추가
	public void insertFavorites(int userId, int restaurantId) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = JDBC.connect();
			System.out.println("connect");
		} catch (Exception e) {
			e.printStackTrace();
		}


		String insertSQL = "INSERT INTO FAVORITES values(?, ?)";
		try {
			pstmt = conn.prepareStatement(insertSQL);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, restaurantId);
			int rowcnt = pstmt.executeUpdate();
			System.out.println(rowcnt + "찜 완료 되었습니다.");
			// conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}

		}
	}
}
