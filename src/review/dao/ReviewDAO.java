package review.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jdbc.JDBC;
import review.dto.ReviewDTO;

public class ReviewDAO {
	private Connection conn = null;
	PreparedStatement pstmt = null;

	public void insertReview(ReviewDTO reviewDTO) {
		String insertSQL = "INSERT INTO reviews (review_id, user_id, restaurant_id, review_content, rating, write_Time) VALUES (reviews_seq.NEXTVAL,?, ?, ?, ?,sysdate)";
		try {
			conn = JDBC.connect();
			pstmt = conn.prepareStatement(insertSQL);
			pstmt.setInt(1, reviewDTO.getId());
			pstmt.setInt(2, reviewDTO.getRestaurantId());
			pstmt.setString(3, reviewDTO.getContent());
			pstmt.setInt(4, reviewDTO.getRating());
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void deleteReview(int id) {
		// reviewId는 review목록. 사용자 정보화면으로 이동하면 사용자가 작성한 리뷰목록이 나옴, 1,2,3..이렇게 그래서 그 번호를
		// 입력받으면 번호에 해당하는 리뷰 삭제
		String deleteSQL = "DELETE FROM reviews WHERE reviewId=?";
		try {
			conn = JDBC.connect();
			pstmt = conn.prepareStatement(deleteSQL);
			// 사용자한테 입력받을 값, 임시로 이렇게 한 거임
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 사용자 화면에서 본인이 작성한 리뷰목록을 보여줌, 1번 리뷰를 선택하면 1번 리뷰를 수정 가능
	public void updateReview(String content, int id) {
		String updateSQL = "UPDATE reviews SET content = ? WHERE id=?";
		try {
			conn = JDBC.connect();
			pstmt = conn.prepareStatement(updateSQL);
			// 사용자한테 입력받을 값, 임시로 이렇게 한 거임
			pstmt.setString(1, content);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// database에서 rating을 얻어와야 함, 그 후 점수별로 review를 보여줘야 함
	public ArrayList<ReviewDTO> selectCategorizedRating(int restaurantId, int rating) {
		String selectCategorizedSQL = "SELECT rating, review_content, write_time from reviews where restaurant_id=? and rating =?";
		ArrayList<ReviewDTO> categorizedReviews = new ArrayList<>();
		try {
			conn = JDBC.connect();
			pstmt = conn.prepareStatement(selectCategorizedSQL);
			// 1: 첫번쩨 물음표, 순서, 받아온 rating값이 ?에 들어감
			pstmt.setInt(1, restaurantId);
			pstmt.setInt(2, rating);
			// select문의 결과를 저장하는 객체
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				ReviewDTO reviewDTO = new ReviewDTO();
				// get으로 가져와서 set으로 dto에 값을 넣어준다
				reviewDTO.setRating(rs.getInt("rating"));
				reviewDTO.setContent(rs.getString("review_content"));
				reviewDTO.setWritingTime(rs.getDate("write_time"));
				categorizedReviews.add(reviewDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return categorizedReviews;
	}

	// restaurant로 이동하면 그 restaurant에 해당하는 review들만 출력
	public ArrayList<ReviewDTO> selectReviewByRestaurant(String restaurantId, int index) {
		String selectByRestaurantSQL = "SELECT userId, content, rating, writingTime FROM reviews WHERE restaurantId = ? LIMIT ?, 10";
		ArrayList<ReviewDTO> restaurantReviews = new ArrayList<>();
		try {
			conn = JDBC.connect();
			pstmt = conn.prepareStatement(selectByRestaurantSQL);
			pstmt.setString(1, restaurantId);
			pstmt.setInt(2, index * 10); // 10개씩 페이징 처리
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				ReviewDTO reviewDTO = new ReviewDTO();
				reviewDTO.setUserId(rs.getInt("userId"));
				reviewDTO.setContent(rs.getString("content"));
				reviewDTO.setRating(rs.getInt("rating"));
				reviewDTO.setWritingTime(rs.getDate("writingTime"));
				restaurantReviews.add(reviewDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return restaurantReviews;
	}

	// 사용자 화면에서 내가 쓴 리뷰들만 출력
	public ArrayList<ReviewDTO> selectReviewByUser(String userId) {
		String selectByUserSQL = "SELECT restaurantId, content, rating, writingTime FROM reviews WHERE userId = ?";
		ArrayList<ReviewDTO> userReviews = new ArrayList<>();
		try {
			conn = JDBC.connect();
			pstmt = conn.prepareStatement(selectByUserSQL);
			pstmt.setString(1, userId);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				ReviewDTO reviewDTO = new ReviewDTO();
				reviewDTO.setRestaurantId(rs.getInt("restaurantId"));
				reviewDTO.setContent(rs.getString("content"));
				reviewDTO.setRating(rs.getInt("rating"));
				reviewDTO.setWritingTime(rs.getDate("writingTime"));
				userReviews.add(reviewDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return userReviews;
	}

	public static void main(String[] args) {
		ReviewDAO reviewDAO = new ReviewDAO();
		List<ReviewDTO> list = reviewDAO.selectCategorizedRating(2,5);
		System.out.println(list);
//		
//		int userId = 1;
//		int restId = 2;
//		int rating = 5;
//		String content = "good";
//		ReviewDTO dto = new ReviewDTO(0, content, rating, null, userId, restId);
//		reviewDAO.insertReview(dto);
		
	}
}
