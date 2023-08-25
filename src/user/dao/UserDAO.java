package user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc.JDBC;
import user.dto.UserDTO;

public class UserDAO {
	
	public void join(UserDTO uDTO) {
	//4. SQL구문 송신
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn=JDBC.connect();
			System.out.println("connect");
		} catch (Exception e) {
			e.printStackTrace();
		}
	    if (!isPasswordValid(uDTO.getPassword())) {
	        System.out.println("유효하지 않은 비밀번호입니다. 회원 가입 실패");
	        return;
	    }		
		
		String insertSQL="INSERT INTO users ( USER_ID, ID, PASSWORD, USER_NAME, GENDER,STATUS,ZIPCODE ) \r\n"
				+ "            values (user_seq.NEXTVAL,        ?,          ?,           ?,     ?,     1,     ?)";
		try {
    	pstmt=conn.prepareStatement(insertSQL);
    	pstmt.setString(1, uDTO.getId());
    	pstmt.setString(2, uDTO.getPassword());
    	pstmt.setString(3, uDTO.getUserName());
    	pstmt.setInt(4, uDTO.getGender());
    	pstmt.setString(5, uDTO.getZipcode());
    	int rowcnt = pstmt.executeUpdate();
    	System.out.println(rowcnt+"건 추가 성공");
    	//conn.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
     	 if(pstmt != null) {
     		 try {
     			 pstmt.close();
     		 } catch (SQLException e) {
     			 // TODO Auto-generated catch block
             
     		 }
     	 }
       if(conn!= null) {
          try {
             conn.close();
          }catch (SQLException e) {
       }
       }
   
    }
		
 }
	
	public boolean isPasswordValid(String password) {
		   // 비밀번호는 최소 8자 이상, 대문자, 소문자, 숫자, 특수문자가 모두 포함되어야 함
	    String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
	    return password.matches(regex);
	}

	public void login(UserDTO uDTO) {
		//4. SQL구문 송신
			PreparedStatement pstmt = null;
			Connection conn = null;
			ResultSet rs=null;
			try {
				conn=JDBC.connect();
				System.out.println("connect");
			} catch (Exception e) {
				e.printStackTrace();
			}
			String selectSQL = "SELECT * From users where id=? and password=?";

			try {
		    	pstmt=conn.prepareStatement(selectSQL);
		    	pstmt.setString(1, uDTO.getId());
		    	pstmt.setString(2, uDTO.getPassword());
		    	rs = pstmt.executeQuery();
		    	 ResultSet resultSet = pstmt.executeQuery();
		    	//conn.rollback();
		    	  if (resultSet.next()) {
		                System.out.println("로그인 성공");
		            } else {
		                System.out.println("로그인 실패");
		            }
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
		     	 if(pstmt != null) {
		     		 try {
		     			 pstmt.close();
		     		 } catch (SQLException e) {
		     			 // TODO Auto-generated catch block
		             
		     		 }
		     	 }
		       if(conn!= null) {
		          try {
		             conn.close();
		          }catch (SQLException e) {
		       }
		       }
		   
		    }
				
		 }
	public void deleteUser(UserDTO uDTO) {
	//4. SQL구문 송신
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn=JDBC.connect();
			System.out.println("connect");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String deleteSQL="update users"
				+ "set status=2"
				+ "where id=? and password=?";
		try {
    	pstmt=conn.prepareStatement(deleteSQL);
    	pstmt.setString(1, uDTO.getId());
    	pstmt.setString(2, uDTO.getPassword());
    	int rowcnt = pstmt.executeUpdate();
    	System.out.println(rowcnt+"건 추가 성공");
    	//conn.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
     	 if(pstmt != null) {
     		 try {
     			 pstmt.close();
     		 } catch (SQLException e) {
     			 // TODO Auto-generated catch block
             
     		 }
     	 }
       if(conn!= null) {
          try {
             conn.close();
          }catch (SQLException e) {
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

		String updateSQL = "UPDATE users SET password=? WHERE id=?";

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
	
	
	public void selectUser(String userId) {
	
	}
	

	public static void main(String[] args) {
//		String id="id1";
//		String password = "1233";
//		String userName = "이";
//		int gender=1;
//		String zipcode="07217";
//		UserDTO uDTO = new UserDTO();

//		public void join(String id, int userId, String password, String userName, int gerder, int status, String zipcode) {

		
		//객체 생성해서 로그인 테스트
		UserDAO uDAO = new UserDAO();
		UserDTO uDTO = new UserDTO();
//		uDAO.join(uDTO);
//		uDTO.setId("id1");
//		uDTO.setPassword("1233");
//		uDAO.login(uDTO);
		uDAO.updateUser(uDTO);
	}
}

	



