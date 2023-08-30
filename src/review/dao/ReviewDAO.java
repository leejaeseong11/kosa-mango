package review.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import exception.AddException;
import exception.FindException;
import exception.ModifyException;
import exception.RemoveException;
import jdbc.JDBC;
import review.dto.ReviewDTO;

public class ReviewDAO {
	private int reviewCount;

	public int getReviewCount() {
		return reviewCount;
	}

	/**
	 * review_id는 oralce sequence로 채운다
	 * 
	 * @param reviewDTO
	 * @throws AddException
	 */
	public void insertReview(ReviewDTO reviewDTO) throws AddException {
		String insertSQL = "INSERT INTO reviews (review_id, review_content, rating, write_Time, restaurant_id, user_id) VALUES (reviews_seq.NEXTVAL,?, ?,sysdate,?,?)";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = JDBC.connect();
			pstmt = conn.prepareStatement(insertSQL);
			pstmt.setString(1, reviewDTO.getContent());
			pstmt.setInt(2, reviewDTO.getRating());
			pstmt.setInt(3, reviewDTO.getRestaurantId());
			pstmt.setInt(4, reviewDTO.getUserId());
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new AddException("DB 연결에 실패했습니다.");
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					throw new AddException("DB 종료에 실패했습니다.");
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					throw new AddException("DB 종료에 실패했습니다.");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new AddException("DB 종료에 실패했습니다.");
				}
			}
		}
	}

	/**
	 * review_id를 매개변수로 받아서 목록의 review번호와 일치하면 삭제 reviewId는 review목록. 사용자 정보화면으로
	 * 이동하면 사용자가 작성한 리뷰목록이 나옴, 1,2,3..이렇게 그래서 그 번호를 입력받으면 번호에 해당하는 리뷰 삭제
	 * 
	 * @param reviewId review목록의 번호
	 * @throws RemoveException
	 */
	public void deleteReview(int reviewId) throws RemoveException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String deleteSQL = "DELETE FROM reviews WHERE review_id=?";
		try {
			conn = JDBC.connect();
			pstmt = conn.prepareStatement(deleteSQL);
			pstmt.setInt(1, reviewId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new RemoveException("DB 연결에 실패했습니다.");
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					throw new RemoveException("DB 종료에 실패했습니다.");
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					throw new RemoveException("DB 종료에 실패했습니다.");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new RemoveException("DB 종료에 실패했습니다.");
				}
			}
		}
	}

	/**
	 * 사용자 화면에서 본인이 작성한 리뷰목록을 보여줌, 1번 리뷰를 선택하면 1번 리뷰를 수정 가능
	 * 
	 * @param content  review내용
	 * @param rating
	 * @param reviewId
	 * @throws ModifyException
	 */
	public void updateReview(String content, int rating, int reviewId) throws ModifyException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String updateSQL = "UPDATE reviews SET review_content = ?, write_Time=sysdate, rating=? WHERE review_id=?";
		try {
			conn = JDBC.connect();
			pstmt = conn.prepareStatement(updateSQL);
			pstmt.setString(1, content);
			pstmt.setInt(2, rating);
			pstmt.setInt(3, reviewId);
			pstmt.executeUpdate();
		} catch (Exception e) {
			throw new ModifyException("DB 연결에 실패했습니다.");
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					throw new ModifyException("DB 종료에 실패했습니다.");
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					throw new ModifyException("DB 종료에 실패했습니다.");
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					throw new ModifyException("DB 종료에 실패했습니다.");
				}
			}
		}
	}

	/**
	 * database에서 rating을 얻어와야 함, 그 후 점수별로 review를 보여줘야 함, pageSize는 목록의 번호, main
	 * class로부터 받아옴
	 * 
	 * @param pageSize
	 * @param rating
	 * @param restaurantId
	 * @param index
	 * @param reviewCount
	 * @return 점수별로 review 출력
	 * @throws FindException
	 */
	public ArrayList<ReviewDTO> selectCategorizedRating(int rating, int restaurantId)
			throws FindException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectCategorizedSQL = "SELECT rating, review_content, write_time FROM reviews WHERE restaurant_id=? AND rating =?";
		ArrayList<ReviewDTO> categorizedReviews = new ArrayList<>();
		try {
			conn = JDBC.connect();
			pstmt = conn.prepareStatement(selectCategorizedSQL);
			// 1: 첫번쩨 물음표, 순서, 받아온 rating값이 ?에 들어감
			pstmt.setInt(1, restaurantId);
			pstmt.setInt(2, rating);
			rs = pstmt.executeQuery();
			// main에서 sdf method를 사용하면 리뷰에 시간까지 출력 가능
			while (rs.next()) {
				ReviewDTO reviewDTO = new ReviewDTO();
				// get으로 가져와서 set으로 dto에 값을 넣어준다
				reviewDTO.setRating(rs.getInt("rating"));
				reviewDTO.setContent(rs.getString("review_content"));
				reviewDTO.setWritingTime(rs.getDate("write_time"));
				categorizedReviews.add(reviewDTO);
			}
			selectCategorizedSQL = "SELECT COUNT(*) FROM reviews WHERE restaurant_id=? AND rating =?";
			pstmt = conn.prepareStatement(selectCategorizedSQL);
			pstmt.setInt(1, restaurantId);
			pstmt.setInt(2, rating);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				this.reviewCount = rs.getInt("COUNT(*)");
			}
		} catch (Exception e) {
			throw new FindException("DB  연결에 실패했습니다.");
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
		return categorizedReviews;
	}

	/**
	 * restaurant로 이동하면 그 restaurant에 해당하는 review들만 출력
	 * 
	 * @param pageSize
	 * @param restaurantId
	 * @param index
	 * @param reviewCount
	 * @return
	 * @throws FindException
	 */
	public ArrayList<ReviewDTO> selectReviewByRestaurant(int pageSize, int restaurantId, int index)
			throws FindException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectByRestaurantSQL = "SELECT *" + " FROM (SELECT ROWNUM rn, a.*" + " FROM ("
				+ " SELECT user_id, review_content, rating, write_time" + " FROM reviews "
				+ " WHERE restaurant_id = ?)  a" + ")" + " WHERE rn BETWEEN ? AND ?";
		ArrayList<ReviewDTO> restaurantReviews = new ArrayList<>();

		int sizeA = 1 + pageSize * (index - 1);
		int sizeB = pageSize * index;
		try {
			conn = JDBC.connect();
			pstmt = conn.prepareStatement(selectByRestaurantSQL);
			pstmt.setInt(1, restaurantId);
			pstmt.setInt(2, sizeA);
			pstmt.setInt(3, sizeB);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				ReviewDTO reviewDTO = new ReviewDTO();
				reviewDTO.setUserId(rs.getInt("user_id"));
				reviewDTO.setContent(rs.getString("review_content"));
				reviewDTO.setRating(rs.getInt("rating"));
				reviewDTO.setWritingTime(rs.getDate("write_time"));
				restaurantReviews.add(reviewDTO);
			}
			selectByRestaurantSQL = "SELECT COUNT(*) FROM reviews WHERE restaurant_id = ?";
			pstmt = conn.prepareStatement(selectByRestaurantSQL);
			pstmt.setInt(1, restaurantId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				this.reviewCount = rs.getInt("COUNT(*)");
			}
		} catch (Exception e) {
			throw new FindException("DB 연결에 실패했습니다.");
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
		return restaurantReviews;
	}

	/**
	 * 사용자 화면에서 내가 쓴 리뷰들만 출력
	 * 
	 * @param pageSize
	 * @param userId
	 * @param index
	 * @param reviewCount
	 * @return
	 * @throws FindException
	 */
	public ArrayList<ReviewDTO> selectReviewByUser(int pageSize, int userId, int index) throws FindException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String selectByUserSQL = "SELECT *" + " FROM (SELECT ROWNUM rn, a.*" + " FROM ("
				+ " SELECT user_id, review_content, rating, write_time, restaurant_id" + " FROM reviews " + " WHERE user_id = ?)  a"
				+ ")" + " WHERE rn BETWEEN ? AND ?";
		ArrayList<ReviewDTO> userReviews = new ArrayList<>();
		int sizeA = 1 + pageSize * (index - 1);
		int sizeB = pageSize * index;
		try {
			conn = JDBC.connect();
			pstmt = conn.prepareStatement(selectByUserSQL);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, sizeA);
			pstmt.setInt(3, sizeB);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ReviewDTO reviewDTO = new ReviewDTO();
				reviewDTO.setRestaurantId(rs.getInt("restaurant_id"));
				reviewDTO.setContent(rs.getString("review_content"));
				reviewDTO.setRating(rs.getInt("rating"));
				reviewDTO.setWritingTime(rs.getDate("write_time"));
				reviewDTO.setUserId(rs.getInt("user_id"));
				userReviews.add(reviewDTO);
			}
			selectByUserSQL = "SELECT COUNT(*) FROM reviews WHERE user_id = ?";
			pstmt = conn.prepareStatement(selectByUserSQL);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				this.reviewCount = rs.getInt("COUNT(*)");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new FindException("DB 연결에 실패했습니다.");
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
		return userReviews;
	}
}
