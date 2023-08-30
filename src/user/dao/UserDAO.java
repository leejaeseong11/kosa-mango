package user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import exception.AddException;
import exception.FindException;
import exception.ModifyException;
import jdbc.JDBC;
import region.dto.RegionDTO;
import user.dto.UserDTO;

public class UserDAO {
	public void join(UserDTO uDTO) throws AddException, FindException {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = JDBC.connect();
		} catch (Exception e) {
			throw new AddException("DB 연결에 실패했습니다.");
		}

		if (!isIdValid(uDTO.getUserId())) {
			throw new AddException("아이디가 중복되었습니다.");
		}

		if (!isPasswordValid(uDTO.getPassword())) {
			throw new AddException("비밀번호는 8자리 이상이어야 하며, 반드시 숫자가 한 개 이상 포함되어야 합니다.");
		}

		String insertSQL = "INSERT INTO users ( USER_ID, ID, PASSWORD, USER_NAME, GENDER,STATUS,ZIPCODE ) \r\n"
				+ "            values (seq_user_id.NEXTVAL,        ?,          ?,           ?,     ?,     1,     ?)";
		try {
			pstmt = conn.prepareStatement(insertSQL);
			pstmt.setString(1, uDTO.getUserId());
			pstmt.setString(2, uDTO.getPassword());
			pstmt.setString(3, uDTO.getUserName());
			pstmt.setInt(4, uDTO.getGender());
			pstmt.setString(5, uDTO.getZipcode());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new AddException("회원 가입에 실패했습니다.");
		} finally {
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

	private boolean isPasswordValid(String password){
		// 최소 8자 이상, 숫자 1개 이상
		String regex = "^(?=.*\\d).{8,}$";
		return password.matches(regex);
	}

	private boolean isIdValid(String id) throws FindException{
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = JDBC.connect();
		} catch (Exception e) {
			throw new FindException("DB 연결에 실패했습니다.");
		}

		String selectSQL = "SELECT COUNT(*) FROM users WHERE id = ? AND STATUS = 1";
		try {
			pstmt = conn.prepareStatement(selectSQL);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) > 0) {
					return false;
				} else {
					return true;
				}
			}

		} catch (SQLException e) {
			throw new FindException("DB 조회에 실패했습니다.");
		} finally {
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
		return true;
	}

	public UserDTO login(UserDTO uDTO) throws FindException {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		UserDTO user = null;

		try {
			conn = JDBC.connect();
		} catch (Exception e) {
			throw new FindException("DB 연결에 실패했습니다.");
		}
		String selectSQL = "SELECT * FROM users WHERE ID=? AND PASSWORD=? AND STATUS = 1";

		try {
			pstmt = conn.prepareStatement(selectSQL);
			pstmt.setString(1, uDTO.getUserId());
			pstmt.setString(2, uDTO.getPassword());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				user = new UserDTO();
				user.setId(rs.getInt(1));
				user.setUserId(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setUserName(rs.getString(4));
				user.setGender(rs.getInt(5));
				user.setZipcode(rs.getString(7));
			}
		} catch (SQLException e) {
			throw new FindException("로그인에 실패했습니다.");
		} finally {
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
		return user;
	}

	public void deleteUser(int userId) throws ModifyException {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = JDBC.connect();
		} catch (Exception e) {
			throw new ModifyException("DB 연결에 실패했습니다.");
		}
		try {
			String updateSQL = "UPDATE users SET status = 2 WHERE user_id = ?";
			pstmt = conn.prepareStatement(updateSQL);
			pstmt.setInt(1, userId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new ModifyException("회원 탈퇴에 실패하셨습니다.") ;
		} finally {
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

	public void updateUser(int userId, String password) throws FindException, ModifyException {
		PreparedStatement pstmt = null;
		Connection conn = null;
		if(!isPasswordValid(password)) {
			throw new FindException("패스워드가 유효하지않습니다.");
		}
		try {
			conn = JDBC.connect();
		} catch (Exception e) {
			throw new ModifyException("DB 연결에 실패했습니다.");
		}

		try {
			String updateSQL = "UPDATE users SET password=? WHERE user_id=? AND STATUS = 1";
			pstmt = conn.prepareStatement(updateSQL);
			pstmt.setInt(2, userId);
			pstmt.setString(1, password);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ModifyException("비밀번호 수정에 실패했습니다.");
		} finally {
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

	public UserDTO selectUser(int userId) throws FindException {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		UserDTO user = null;

		try {
			conn = JDBC.connect();
		} catch (Exception e) {
			throw new FindException("DB 연결에 실패했습니다.");
		}
		String selectSQL = "SELECT u.ID, u.PASSWORD, u.USER_NAME, u.GENDER, r.ZIPCODE, r.CITY_NAME, r.SI_GUN_GU, r.DONG_EUP_MYEON FROM USERS u JOIN REGIONS r ON u.ZIPCODE = r.ZIPCODE WHERE U.id = ? AND u.STATUS = 1";

		try {
			pstmt = conn.prepareStatement(selectSQL);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				user = new UserDTO();
				user.setId(rs.getInt("USER_ID"));
				user.setPassword(rs.getString("PASSWORD"));
				user.setUserName(rs.getString("USER_NAME"));
				user.setGender(rs.getInt("GENDER"));

				RegionDTO region = new RegionDTO();
				region.setZipcode(rs.getString("ZIPCODE"));
				region.setCityName(rs.getString("CITY_NAME"));
				region.setSiGunGu(rs.getString("SI_GUN_GU"));
				region.setDongEupMyeon(rs.getString("DONG_EUP_MYEON"));
				user.setRegion(region);
			}

		} catch (SQLException e) {
			throw new FindException("회원 검색에 실패했습니다.");
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

		return user;
	}
}