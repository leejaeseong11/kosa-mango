package user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import FavoriteDAO.FavoriteDAO;
import exception.AddException;
import exception.FindException;
import exception.ModifyException;
import exception.RemoveException;
import jdbc.JDBC;
import region.dto.RegionDTO;
import user.dto.UserDTO;

public class UserDAO {

	public void join(UserDTO uDTO) throws AddException{
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = JDBC.connect();
			System.out.println("connect");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ���대�� 寃���
		if (!isIdValid(uDTO.getId())) {
			
			throw new AddException("아이디가 중복되었습니다.");
		}

		if (!isPasswordValid(uDTO.getPassword())) {
			throw new AddException("비밀번호가 중복되었습니다."):
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
				if (rs != null) {
					try {
					rs.close();
				} catch (SQLException e) {
				}
			}

		}

	}

	private boolean isPasswordValid(String password){
		// 鍮�諛�踰��몃�� 理��� 8�� �댁��, ��臾몄��, ��臾몄��, �レ��, �뱀��臾몄��媛� 紐⑤�� �ы�⑤���댁�� ��
		String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
		return password.matches(regex);
	}

	// ���대�� 以�蹂듦���
	private boolean isIdValid(String id) throws FindException{
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = JDBC.connect();
			System.out.println("connect");
		} catch (Exception e) {
			throw new FindException("DB연결에 실패했습니다.");
		}

		String selectSQL = "SELECT COUNT(*) FROM users WHERE id = ? AND STATUS = 1";
		try {
			pstmt = conn.prepareStatement(selectSQL);
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				// 議댁�ы��硫� 1�댁�� 由ы��
				if (rs.getInt(1) > 0) {
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
		// 4. SQL援щЦ �≪��
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		UserDTO user = null;	//濡�洹몄�� �ㅽ�⑥�� null return

		try {
			conn = JDBC.connect();
			System.out.println("connect");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String selectSQL = "SELECT * FROM users WHERE ID=? AND PASSWORD=? AND STATUS = 1";

		try {
			pstmt = conn.prepareStatement(selectSQL);
			pstmt.setString(1, uDTO.getId());
			pstmt.setString(2, uDTO.getPassword());
			rs = pstmt.executeQuery();
			// conn.rollback();
			if (rs.next()) {
				user = new UserDTO();
				user.setUserId(rs.getInt(1));
				user.setId(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setUserName(rs.getString(4));
				user.setGender(rs.getInt(5));
				user.setZipcode(rs.getString(7));

			} else {

			}
		} catch (SQLException e) {
			throw new FindException("로그인에 실패하셨습니다");
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
		return user;
	}

	public void deleteUser(UserDTO uDTO) {
		// 4. SQL援щЦ �≪��
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = JDBC.connect();
			System.out.println("connect");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			pstmt = conn.prepareStatement(deleteSQL);
			pstmt.setString(1, uDTO.getId());
			pstmt.setString(2, uDTO.getPassword());
			int rowcnt = pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new RemoveException("회원탈퇴에 실패하셨습니다.") ;
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
		if(!isPasswordValid(uDTO.getpassword))
			throw new ModifyException("패스워드가 유효하지않습니다.");
		try {
			conn = JDBC.connect();
			System.out.println("connect");
		} catch (Exception e) {
		
		}

		String updateSQL = "UPDATE users SET password=? WHERE id=? AND STATUS = 1";

		try {
			pstmt = conn.prepareStatement(updateSQL);
			pstmt.setString(1, uDTO.getPassword());
			pstmt.setString(2, uDTO.getId());

			int rowCnt = pstmt.executeUpdate();

			
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

	//���� 李�������, ���� ���몄��蹂� �대��
	public UserDTO selectUser(String id, String password) {
    PreparedStatement pstmt = null;
    Connection conn = null;
    ResultSet rs = null;
    UserDTO user = null;

    try {
        conn = JDBC.connect();
    } catch (Exception e) {
        e.printStackTrace();
    }
    String selectSQL = "SELECT u.ID, u.PASSWORD, u.USER_NAME, u.GENDER, r.ZIPCODE, r.CITY_NAME, r.SI_GUN_GU, r.DONG_EUP_MYEON FROM USERS u JOIN REGIONS r ON u.ZIPCODE = r.ZIPCODE WHERE U.id = ? AND u.PASSWORD = ? AND u.STATUS = 1";

    try {
        pstmt = conn.prepareStatement(selectSQL);
        pstmt.setString(1, id);
        pstmt.setString(2, password);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            user = new UserDTO();
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

            // 李� 紐⑸� 議고��
            new FavoriteDAO().selectFavoritesByUserId(id);
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

    return user; // 議고���� ���� ��蹂대�� 諛���
}
}