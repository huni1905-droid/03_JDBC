package edu.kh.jdbc.common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

// 연습용
public class LoadXMLFile {

	public static void main(String[] args) {

		// XML 파일 읽어오기 (FileInputStream, Properties)
		
		FileInputStream fis = null;
		Connection conn = null;
		
		try {
			
			Properties prop = new Properties();
			
			// driver.xml 파일을 읽기 위한 InputStream 객체 생성
			fis = new FileInputStream("driver.xml"); // 해당 프로젝트 내에 있는 xml파일 읽어 오겠다.
			
			// 연결된 driver.xml 파일에 있는 내용을 모두 일어와
			// Properties 객체에 K:V 형식으로 저장
			prop.loadFromXML(fis); // InputStream에 있는 driver.xml 을 다 읽는다
			// prop에 아래와 같이 저장되어 있다
			// 	 key	:	value
			// driver 	: oracle.jdbc.driver.OracleDriver
			// url 		: jdbc:oracle:thin:@localhost:1521:XE
			// userName : kh_kjh
			// password : kh1234
		
			
			// prop.getProperty("key") : key가 일치하는 속성값(value)을 얻어옴
			String driver = prop.getProperty("driver"); 	// driver라는 key의 value 값을 갖고온다
			// oracle.jdbc.driver.OracleDriver
			
			String url = prop.getProperty("url");			 // url라는 key의 value 값을 갖고온다
			// jdbc:oracle:thin:@localhost:1521:XE
			
			String userName = prop.getProperty("userName"); // userName라는 key의 value 값을 갖고온다
			// kh_kjh
			
			String password = prop.getProperty("password"); // password라는 key의 value 값을 갖고온다
			// kh1234
			
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userName, password);
			System.out.println(conn); // 잘 작성되었는지 확인(주소창 잘 나왔는지 확인)
			// oracle.jdbc.driver.T4CConnection@29176cc1
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally {
			try {
				if(fis != null) fis.close();
				if(conn != null) conn.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		
		
	}

}
