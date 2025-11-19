package edu.kh.jdbc.model.dao;

// inport static : 지정된 경로에 존재하는 static 구문을 모두 얻어와 
// 클래스명.메서드명() 이 아닌 메서드명() 만 작성해도 호출 가능하게 함
// 편하긴 하지만 제3자가 봤을때 가독성이 떨어진다는 단점이 있다.
// JDBCTemplate에 있는 static을 모두 그냥 호출하겠다
import static edu.kh.jdbc.common.JDBCTemplate.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.kh.jdbc.model.dto.User;

// (Model 중 하나) DAO (Date Access Object : 데이터 접근 객체)
// 데이터가 저장된 곳(DB)에 접근하는 용도의 객체
// -> DB에 접근하여 Java에서 원하는 결과를 얻기 위해
// SQL을 수행하고 결과를 반환받는 역할
public class UserDAO {

	// 필드
	// - DB 접근 관련한 JDBC 객체 참조 변수 선언
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	/** 1. User 등록 DAO 
	 * @param conn : DB 연결 정보가 담겨있는 Connection 객체
	 * @param user : 입력받은 id, pw, name 이 세팅된 User 객체
	 * @return : INSERT 결과 행의 갯수
	 */
	public int insertUser(Connection conn, User user) throws Exception{ // try-catch에서 catch 사용하지 않고 
																		// 나에게 발생하는 모든 Exception 을 던지겠다.
		
		// 1. 결과 저장용 변수 선언
		int result = 0;
		
		try {
			// 2. SQL 작성
			String sql = """
					INSERT INTO TB_USER
					VALUES(SEQ_USER_NO.NEXTVAL, ?, ?, ?, DEFAULT)
					"""; 
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ? 위치홀더에 알맞은 값 대입
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPw());
			pstmt.setString(3, user.getUserName());
			
			// 5. SQL(INSERT) 수행(executeUpdate()) 후 결과(삽입된 행의 갯수) 반환 받기
			result = pstmt.executeUpdate();
			
		} finally {
			
			// 6. 사용한 JDBC 객체 자원 반환
			close(pstmt);
			// 기울임 처리 외어있는것은 static 메서드라는 뜻
			
		}
		
		
		// 결과 저장용 변수에 저장된 최종 값 반환
		return result; // *** 가장 중요!!! ***
		
		
		
		
		
	}

	/** 2. User 전체 조회 DAO
	 * @param conn 
	 * @return : List<User> userList
	 * 
	 */
	public List<User> selectAll(Connection conn) throws Exception{

		// 1. 결과 저장용 변수 선언
		List<User> userList = new ArrayList<User>();
		
		try {
			// 2. SQL 작성
			String sql = """
						SELECT USER_NO, USER_ID, USER_PW, USER_NAME, 
						TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') ENROLL_DATE
						FROM TB_USER
						ORDER BY USER_NO
						"""; // *** 디비버에 작성하고 갖고올때 마지막 ; 도 같이 복사해서 오지 않게 주의!!! ***
			
			// 3. PreparedStatement 생성
			pstmt =conn.prepareStatement(sql);
			
			
			// 4. ? (위치홀더)에 알맞은 값 대입(없으면 패스)
			
			// 5. SQL(SELECT) 수행(executeQuery()) 후 결과 반환(ResultSet) 받기
			rs = pstmt.executeQuery();
			
			
			// 6. 조회 결과(rs)를 1행씩 접근하여 컬럼 값 얻어오기
			// 6-1. 몇 행이 조회될지 모른다 -> while 문
			// 6-1. 무조건 1행만 조회된다	-> if 문
			
			while(rs.next()) {
				
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				String enrollDate = rs.getString("ENROLL_DATE");
				// java.sql.Date 타입으로 값을 저장하지 않은 이유
				// -> SELECT 문에서 TO_CHAR()를 이용하여 문자열 형태로 변환해 조회해왔기 때문.
				
				// User 객체 새로 생성하여 DB에서 얻어온 컬럼값 필드로 세팅
				User user = new User(userNo, userId, userPw, userName, enrollDate);
				
				userList.add(user); // user 객체 만든것 userList에 넣기
				
				
			}
			
		} finally {
			// 7. 사용한 자원 반환
			close(rs);
			close(pstmt);
			
		}
		
		// 조회 결과가 담긴 List 반환
		return userList;
	}


	
	/** 3. User 중 이름에 검색어가 포함된 회원 조회 DAO
	 * @param keyword 
	 * @param conn 
	 * @return serchList
	 * 
	 */
	public List selectName(Connection conn, String keyword) throws Exception{
		
		// 1. 결과 저장용 변수 선언
		List<User> serchList = new ArrayList<User>();
			try {
				
				// 2. SQL 작성
				String sql = """
						SELECT USER_NO, USER_ID, USER_PW, USER_NAME,
						TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"원" DD"일"') ENROLL_DATE
						FROM TB_USER
						WHERE USER_NAME LIKE '%'|| ? ||'%' 
						ORDER BY USER_NO
						"""; 
						// SQL 문 작성시 주의 
						// '%?%'로 작성 하는것 아니다! '%?%'는 ?가 있는 사람 찾는 거야!
						// '%'|| ? ||'%' 
				
				// 3. PreparedStatement 생성
				pstmt = conn.prepareStatement(sql);
				
				// 4. ? (위치홀더)에 알맞은 값 대입(없으면 패스)
				pstmt.setString(1, keyword);
				
				// 5. SQL(SELECT) 수행(executeQuery()) 후 결과 반환(ResultSet) 받기
				rs = pstmt.executeQuery();
				
				// 6. 조회 결과(rs)를 1행씩 접근하여 컬럼 값 얻어오기
				while(rs.next()) {
					int userNo = rs.getInt("USER_NO");
					String userId = rs.getString("USER_ID");
					String userPw = rs.getString("USER_PW");
					String userName = rs.getString("USER_NAME");
					String enrollDate = rs.getString("ENROLL_DATE");
					
					User user = new User(userNo, userId, userPw, userName, enrollDate);
					
					serchList.add(user);
				}
				
				
			} finally {
				// 7. 사용한 자원 반환
				close(rs);
				close(pstmt);
				
			}

			
		return serchList;
	}

	
	/** 4. USER_NO를 입력 받아 일치하는 User 조회 DAO
	 * @param conn 
	 * @param inputNo
	 * @return
	 */
	public User selectUser(Connection conn, int inputNo) throws Exception{

		User user = null;
		
		try {
			String sql = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME,
					TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"원" DD"일"') ENROLL_DATE
					FROM TB_USER
					WHERE USER_NO = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, inputNo);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");		
				String userPw = rs.getString("USER_PW");		
				String userName = rs.getString("USER_NAME");		
				String enrollDate = rs.getString("ENROLL_DATE");		
			
				user = new User(userNo, userId, userPw, userName, enrollDate);
				
			}
			
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		
		return user;
	}

	/** 5. USER_NO를 입력받아 일치하는 USER 삭제 DAO 
	 * @param conn
	 * @param deleteNo
	 * @return
	 */
	public int deleteUser(Connection conn, int deleteNo) throws Exception{

		int result = 0;
		
		try {
			String sql = """
					DELETE FROM TB_USER
					WHERE USER_NO = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, deleteNo);
			result = pstmt.executeUpdate();
			
			
		} finally {
			close(pstmt);
			
		}
		
		
		return result;
	}


	

	/**6-1. ID, PW가 일치하는 회원이 있는지 조회 DAO
	 * @param conn
	 * @param userId
	 * @param userPw
	 * @return
	 */
	public int selectUserNo(Connection conn, String userId, String userPw) throws Exception{
		
		int userNo = 0;
		
		try {
			String sql = """
				SELECT USER_NO
				FROM TB_USER
				WHERE USER_ID = ? 
				AND USER_PW = ?
				""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			pstmt.setString(2, userPw);
			
			rs=pstmt.executeQuery();
			
			// 조회된 행이 1개가 있을 경우
			if(rs.next()) {
				userNo = rs.getInt("USER_NO");
				
			}
			
		} finally {
			close(rs);
			close(pstmt);
			
			
		}
		
		return userNo; // 조회 성공 USER_NO, 실패 0 반환(초기값 0이기 때문에)
	}

	/** 6-2. USER_NO가 일치하는 회원의 이름 수정 DAO
	 * @param conn
	 * @param name
	 * @param userNo
	 * @return
	 */
	public int updateName(Connection conn, String name, int userNo) throws Exception{
		
		int result = 0;
		
		try {
			String sql = """
					UPDATE TB_USER
					SET USER_NAME = ?
					WHERE USER_NO = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, name);
			pstmt.setInt(2, userNo);
			
			result = pstmt.executeUpdate();
			
			
			
			
		} finally {
			close(pstmt);
		}
		return 0;
	}

	/** 7-1. 아이디 중복 확인 DAO
	 * @param conn
	 * @param userId
	 * @return
	 */
	public int idCheck(Connection conn, String userId) throws Exception{
		
		int count = 0;
		
		try {
			
			String sql = """
					SELECT COUNT(*)
					FROM TB_USER
					WHERE USER_ID = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				count = rs.getInt(1); 	// 조회된 컬럼의 순서 번호를 이용해 
										// 컬럼값 얻어오는것도 가능하다.
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return count;
	}
	
	
	
	
	
	
	
	
}
