package user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import FavoriteDAO.FavoriteDAO;
import jdbc.JDBC;
import region.dto.RegionDTO;
import user.dto.UserDTO;

public class UserDAO {

	public void join(UserDTO uDTO) {
		// 4. SQL구문 송신
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = JDBC.connect();
			System.out.println("connect");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 아이디 검사
		if (!isIdValid(uDTO.getId())) {
			System.out.println("존재하는 아이디 입니다.");
			return;
		}

		if (!isPasswordValid(uDTO.getPassword())) {
			System.out.println("유효하지 않은 비밀번호입니다. 회원 가입 실패");
			return;
		}

		String insertSQL = "INSERT INTO users ( USER_ID, ID, PASSWORD, USER_NAME, GENDER,STATUS,ZIPCODE ) \r\n"
				+ "            values (seq_user_id.NEXTVAL,        ?,          ?,           ?,     ?,     1,     ?)";
		try {
			pstmt = conn.prepareStatement(insertSQL);
			pstmt.setString(1, uDTO.getId());
			pstmt.setString(2, uDTO.getPassword());
			pstmt.setString(3, uDTO.getUserName());
			pstmt.setInt(4, uDTO.getGender());
			pstmt.setString(5, uDTO.getZipcode());
			int rowcnt = pstmt.executeUpdate();
			System.out.println(rowcnt + "건 추가 성공");
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

	public boolean isPasswordValid(String password) {
		// 비밀번호는 최소 8자 이상, 대문자, 소문자, 숫자, 특수문자가 모두 포함되어야 함
		String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
		return password.matches(regex);
	}

	// 아이디 중복검사
	public boolean isIdValid(String id) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = JDBC.connect();
			System.out.println("connect");
		} catch (Exception e) {
			e.printStackTrace();
		}

		String selectSQL = "SELECT COUNT(*) FROM users WHERE id = ? AND STATUS = 1";
		try {
			pstmt = conn.prepareStatement(selectSQL);
			pstmt.setString(1, id);
			ResultSet rset = pstmt.executeQuery();
			while (rset.next()) {
				// 존재하면 1이상 리턴
				if (rset.getInt(1) > 0) {
					return false;
				} else {
					return true;
				}

			}

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
		return true;
	}

	public UserDTO login(UserDTO uDTO) {
		// 4. SQL구문 송신
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		UserDTO user = null;	//로그인 실패시 null return

		try {
			conn = JDBC.connect();
			System.out.println("connect");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String selectSQL = "SELECT * From users where id=? and password=? AND STATUS = 1";

		try {
			pstmt = conn.prepareStatement(selectSQL);
			pstmt.setString(1, uDTO.getId());
			pstmt.setString(2, uDTO.getPassword());
			rs = pstmt.executeQuery();
			// conn.rollback();
			if (rs.next()) {
				System.out.println("로그인 성공");

				user = new UserDTO();
				user.setUserId(rs.getInt(1));
				user.setId(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setUserName(rs.getString(4));
				user.setGender(rs.getInt(5));
				user.setZipcode(rs.getString(7));

			} else {
				System.out.println("로그인 실패");

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
		return user;
	}

	public void deleteUser(UserDTO uDTO) {
		// 4. SQL구문 송신
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = JDBC.connect();
			System.out.println("connect");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String deleteSQL = "update users" + " set status=2" + " where id=? and password=? AND STATUS = 1";
		try {
			pstmt = conn.prepareStatement(deleteSQL);
			pstmt.setString(1, uDTO.getId());
			pstmt.setString(2, uDTO.getPassword());
			int rowcnt = pstmt.executeUpdate();
			System.out.println(rowcnt + "건 추가 성공");
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

	public void updateUser(UserDTO uDTO) {
		PreparedStatement pstmt = null;
		Connection conn = null;

		try {
			conn = JDBC.connect();
			System.out.println("connect");
		} catch (Exception e) {
			e.printStackTrace();
		}

		String updateSQL = "UPDATE users SET password=? WHERE id=? AND STATUS = 1";

		try {
			pstmt = conn.prepareStatement(updateSQL);
			pstmt.setString(1, uDTO.getPassword());
			pstmt.setString(2, uDTO.getId());

			int rowCnt = pstmt.executeUpdate();

			if (rowCnt > 0) {
				System.out.println(rowCnt + "건 수정 성공");
			} else {
				System.out.println("수정 실패");
			}
		} catch (SQLException e) {
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

	//나의 찜한식당, 회원 상세정보 열람
	public void selectUser(String id, String password) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = JDBC.connect();
			System.out.println("connect");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String selectSQL = "SELECT u.ID, u.PASSWORD, u.USER_NAME, u.GENDER, r.ZIPCODE, r.CITY_NAME, r.SI_GUN_GU, r.DONG_EUP_MYEON FROM USERS u JOIN REGIONS r ON u.ZIPCODE = r.ZIPCODE WHERE U.id = ? AND u.PASSWORD = ? AND u.STATUS = 1";

		try {
			pstmt = conn.prepareStatement(selectSQL);
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				UserDTO user = new UserDTO();
				user.setId(rs.getString("id"));
				user.setPassword(rs.getString("PASSWORD"));
				user.setUserName(rs.getString("USER_NAME"));
				user.setGender(rs.getInt("GENDER"));

				RegionDTO region = new RegionDTO();
				region.setZipcode(rs.getString("ZIPCODE"));
				region.setCityName(rs.getString("CITY_NAME"));
				region.setSiGunGu(rs.getString("SI_GUN_GU"));
				region.setDongEupMyeon(rs.getString("DONG_EUP_MYEON"));

				user.setRegion(region);

				System.out.println("회원상세정보");
				System.out.println(user);

				System.out.println("회원 찜 목록");
				new FavoriteDAO().selectFavoritesByUserId(id);
			}else {
				System.out.println("비밀번호가 잘못되었습니다.");
				return;
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
	}

	public static void main(String[] args) {
//      String id="id1";
//      String password = "1233";
//      String userName = "이";
//      int gender=1;
//      String zipcode="07217";
//      UserDTO uDTO = new UserDTO();

//      public void join(String id, int userId, String password, String userName, int gerder, int status, String zipcode) {

		// 객체 생성해서 로그인 테스트
		UserDAO uDAO = new UserDAO();
		UserDTO userInfo = new UserDTO();
//      uDAO.join(uDTO);
//      uDTO.setId("id1");
//      uDTO.setPassword("12334");
		// uDAO.login(uDTO);
		// uDAO.updateUser(uDTO);
//      uDAO.deleteUser(uDTO);

		// 비밀번호는 최소 8자 이상, 대문자, 소문자, 숫자, 특수문자가 모두 포함되어야 함
		// 회원 형식 USER_ID, ID, PASSWORD, USER_NAME, GENDER,STATUS,ZIPCODE
//		userInfo = new UserDTO("id1", "Test1234!", "test11111", 2, "07217");// 가입 데이터
//		uDAO.join(userInfo);

		uDAO.selectUser("danbi52600", "danbi123");	//회원 상세보기

	}

}