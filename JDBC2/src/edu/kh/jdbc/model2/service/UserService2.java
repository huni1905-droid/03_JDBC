package edu.kh.jdbc.model2.service;

import java.sql.Connection;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.model.dto.User;
import edu.kh.jdbc.model2.dao.UserDAO2;

//(Model 중 하나) Service : 비즈니스 로직을 처리하는 계층, 
//데이터를 가공하고 트랜잭션(commit, rollback) 관리 수행
public class UserService2 {

	// 필드
	private UserDAO2 dao = new UserDAO2();

	/** 1. User 등록 서비스
	 * @param user
	 * @return
	 * @throws Exception 
	 * @throws  
	 */
	public int insertUser(User user) throws Exception  {
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.insertUser(conn, user);
		
		
		
		return 0;
	}
	
	
	

}
