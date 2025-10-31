package edu.kh.jdbc.model2.dao;

import static edu.kh.jdbc.common.JDBCTemplate.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.kh.jdbc.model.dto.User;

// (Model 중 하나) DAO (Date Access Object : 데이터 접근 객체)
// 데이터가 저장된 곳(DB)에 접근하는 용도의 객체
// -> DB에 접근하여 Java에서 원하는 결과를 얻기 위해
// SQL을 수행하고 결과를 반환받는 역할
public class UserDAO2 {

	// 필드
	// - DB 접근 관련한 JDBC 객체 참조 변수 선언
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	
	
	
	/** 1. User 등록 DAO2
	 * @param conn
	 * @param user
	 * @return
	 * @throws SQLException 
	 */
	public int insertUser(Connection conn, User user) throws SQLException {
		
		int result = 0;
		try {

			String sql = """
				INSERT TB_USER
				VALUES(SEQ_USER_NO.NEXTVAL, ?, ?, ?, DAFAULT) 
				""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPw());
			pstmt.setString(3, user.getUserName());
			
			result = pstmt.executeUpdate();
			
			
		} finally {
			
			close(pstmt);
			
		}
		
		
		
		
		
		
		
		return 0;
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
}
