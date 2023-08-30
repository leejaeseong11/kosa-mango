package FavoriteDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import exception.AddException;
import exception.FindException;
import jdbc.JDBC;
import restaurant.dto.RestaurantDTO;

public class FavoriteDAO {

	// 회원아이디 찜 식당 조회
	public ArrayList<RestaurantDTO> selectFavoritesByUserId(int userId) throws FindException {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		ArrayList<RestaurantDTO> list = new ArrayList<>();

		try {
			conn = JDBC.connect();
		} catch (Exception e) {
			throw new FindException("DB 연결에 실패했습니다.");
		}
		String selectSQL = "SELECT r.RESTAURANT_NAME, r.RESTAURANT_ID FROM FAVORITES f JOIN USERS u ON f.USER_ID = u.USER_ID JOIN RESTAURANTS r ON f.RESTAURANT_ID = r.RESTAURANT_ID "
				+ "WHERE u.user_id = ?";

		try {
			pstmt = conn.prepareStatement(selectSQL);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				RestaurantDTO rDTO = new RestaurantDTO();
				rDTO.setName(rs.getString(1));
				rDTO.setId(rs.getInt(2));
				list.add(rDTO); // 검색 결과 리스트 추가
			}

		} catch (SQLException e) {
			throw new FindException("찜 목록 조회에 실패했습니다.");
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					throw new FindException("DB 연결에 실패했습니다.");
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					throw new FindException("DB 연결에 실패했습니다.");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new FindException("DB 연결에 실패했습니다.");
				}
			}
		}

		return list;
	}

	// 찜 추가
	public void insertFavorites(int userId, int restaurantId) throws AddException {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = JDBC.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String insertSQL = "INSERT INTO FAVORITES values(?, ?)";
		try {
			pstmt = conn.prepareStatement(insertSQL);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, restaurantId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AddException("찜 목록 입력에 실패했습니다.");
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					throw new AddException("DB 연결에 실패했습니다.");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new AddException("DB 연결에 실패했습니다.");
				}
			}

		}
	}

	// 삭제
	public void deleteFavorites(int userId, int restaurantId) throws AddException {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = JDBC.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String insertSQL = "DELETE FROM favorites WHERE restaurant_id = ? AND user_id = ?";
		try {
			pstmt = conn.prepareStatement(insertSQL);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, restaurantId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AddException("찜 목록 삭제에 실패했습니다.");
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					throw new AddException("DB 연결에 실패했습니다.");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new AddException("DB 연결에 실패했습니다.");
				}
			}
		}
	}
}
