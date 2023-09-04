package jdbc;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class JDBC {
    public static Connection connect() throws Exception{
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new Exception("JDBC 드라이버를 찾을 수 없습니다.");
        }

        Connection conn = null;
        Properties env = new Properties();
        try {
            env.load(JDBC.class.getResourceAsStream("../application.properties"));
        } catch (IOException e) {
            throw new Exception("application.properties 파일을 찾지 못했습니다.");
        }
        String url = env.getProperty("jdbc.url");
        String user = env.getProperty("jdbc.user");
        String password = env.getProperty("jdbc.password");
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new Exception("DB 연결에 실패했습니다.");
        }

        return conn;
    }
}