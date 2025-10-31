package edu.kh.jdbc.common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/* JDBCTemplate : 
 * JDBC 관련 작업을 위한 코드를 미리 작성해서 제공하는 클래스
 * 
 * - Connection 생성
 * - AutoCommit false
 * - commit / rollback
 * - 각종 자운 반환 close()
 * 
 * ******* 중요 *******
 * 어디에서든지 JDBCTemplate 클래스를 
 * 객체로 만들지 않고도 메서드를 사용 할 수 있도록 하기 위해
 * 모든 메서드를 public static 으로 선언
 * 
 * cf) static -> 객체 생성 없이 필드, 메서드 접근 가능 
 * ex) Math.random() 와 같이 사용 == Math math = new Math(); 하지 않아도 사용 가능한것
 * 
 * */
public class JDBCTemplate {

	// 필드
	private static Connection conn = null; // 참조변수 선언만 함
	
	
	// 메서드
	
	/** ale + shift + J
	 * 호출 시 Connection 객체를 생성하여 호출한 곳으로 반환하는 메서드
	 * + Autocommit 끄기
	 * 
	 * @return conn
	 */
	public static Connection getConnection() {
		try {
			
			// 이전에 Connection 객체가 만들어졌고(존재하고)
			// 아직 close() 된 상태가 아니라면
			// 새로 만들지 않고, 기존 Connection 반환
			if( conn != null && !conn.isClosed() ) return conn;
			
			// 1. Properties 객체 생성
			Properties prop = new Properties();
			
			// 2. Properties가 재공하는 메서드를 이용해서 driver.xml 파일 내용을 읽어오기
			prop.loadFromXML(new FileInputStream("driver.xml")); //driver.xml 를 읽어와서 prop에 저장하겠다
			
			// 3. prop 에 저장된 값을 이용해서 Connection 객체 생성
			Class.forName(prop.getProperty("driver"));
			// Class.forName("oracle.jdbc.driver.OracleDriver")
			
			conn = DriverManager.getConnection(prop.getProperty("url"),		 // "jdbc:oracle:thin:@localhost:1521:XE"
											   prop.getProperty("userName"), // "kh_kjh"
											   prop.getProperty("password"));// "kh1234"
			
			// 4. 만들어진 Connection 에서 AutoCommit 끄기
			conn.setAutoCommit(false);
			
			
		} catch (Exception e) {
			System.out.println("커넥션 생성 중 예외 발생(JDBCTemplate의 getConnection())");
			e.printStackTrace();
		}
		
		return conn;
		
	}
	
	
	/** 전달 받은 커넥션에서 수행한 SQL을 commit 하는 메서드
	 * 
	 */
	public static void commit(Connection conn) {
		
		try {
			if(conn != null && !conn.isClosed()) conn.commit();
			
			
		} catch (Exception e) {
			System.out.println("커밋 중 예외 발생");
			e.printStackTrace();
		}
		
	}
	
	
	/** 전달 받은 커넥션에서 수행한 SQL을 rollback 하는 메서드
	 * @param conn
	 */
	public static void rollback(Connection conn) {
		
		try {
			if(conn != null && !conn.isClosed()) conn.rollback();
			
		} catch (Exception e) {
			System.out.println("롤백 중 예외 발생");
			e.printStackTrace();
		}
	}
	
	
	// -----------------------------------------------------------
	
	// Connection, Statement(PreparedStatement), ResultSet
	
	/** 전달받은 커넥션을 close(자원반환) 하는 메서드
	 * 
	 */
	public static void close(Connection conn) {
		
		try {
			if(conn != null && !conn.isClosed()) conn.close();
			
		} catch (Exception e) {
			System.out.println("커넥션 close() 중 예외 발생");
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
	
	// 오버로딩? 
	// 	: 같은 이름의 메서드 과적/ 
	// 규칙: 매개변수의 타입, 순서, 갯수 중 하나라도 달라야 한다
	
	/** 전달 받은 Statement or PreparedStatement 둘 다 close() 할 수 있는 메서드
	 * + 다형성의 업캐스팅 적용
	 * -> PreparedStatement는 Statement의 자식 -> 부모 클래스인 Statement만 닫아도 자식 PreparedStatement 따로 안 닫아도 닫힌다
	 */
	public static void close(Statement stmt) {
		
		try {
			if( stmt!=null && !stmt.isClosed()) stmt.close();
			
		} catch (Exception e) {
			System.out.println("Statement close() 중 예외 발생");
			e.printStackTrace();
			
		}
	}
	
	
	
	
	/** 전달 받은 ResultSet을 close() 하는 매서드
	 * 
	 */
	public static void close(ResultSet rs) {
		try {
			if( rs != null && !rs.isClosed()) rs.close();
			
		} catch (Exception e) {
			System.out.println("Resultset close() 중 예외 발생");
			e.printStackTrace();
			
		}
	}
	
	
	
	
	
	
	
}
