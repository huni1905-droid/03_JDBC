package edu.kh.jdbc.model.dao;

import static edu.kh.jdbc.common.JDBCTemplate.close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.kh.jdbc.model.dto.Student;

public class StudentDAO {

	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	
	/** 1. 새로운 학생 정보 등록 DAO
	 * @param conn
	 * @param std
	 * @return
	 * @throws Exception
	 */
	public int insertStd(Connection conn, Student std) throws Exception{

		int result = 0;
		
		try {
			String sql = """
					INSERT INTO KH_STUDENT
					VALUES(STD_NO.NEXTVAL, ?, ?, ?, DEFAULT )
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, std.getStdName());
			pstmt.setInt(2, std.getStdAge());
			pstmt.setString(3, std.getMajor());
		
			result = pstmt.executeUpdate();
			
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	/** 2. 모든 학생 정보 조회 DAO
	 * @param conn 
	 * @return
	 */
	public List<Student> selectAll(Connection conn) throws Exception {
		List<Student> stdList = new ArrayList<Student>();
		
		try {
			String sql = """
					SELECT STD_NO, STD_NAME, STD_AGE, MAJOR,
					TO_CHAR(ENT_DATE, 'YYYY"년" MM"월" DD"일"') ENT_DATE
					FROM KH_STUDENT
					ORDER BY STD_NO
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int stdNo = rs.getInt("STD_NO");
				String stdName = rs.getString("STD_NAME");
				int stdAge = rs.getInt("STD_AGE");
				String major = rs.getString("MAJOR");
				String entDatd = rs.getString("ENT_DATE");
				
				Student std = new Student(stdNo, stdName, stdAge, major, entDatd);
				stdList.add(std);
			}
					
		}finally {
			close(rs);
			close(pstmt);
		}
		
		
		return stdList;
	}

	/** 4. 한번 기준 학생 정보 삭제 DAO
	 * @param conn
	 * @param deleteNo
	 * @return
	 * @throws Exception
	 */
	public int deleteStd(Connection conn, int deleteNo) throws Exception{
		
		int result = 0;
		
		try {
			String sql = """
					DELETE FROM KH_STUDENT
					WHERE STD_NO = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, deleteNo);
			
			result = pstmt.executeUpdate();
			
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	/** 5. 전공별 학생 조회 DAO
	 * @param conn
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public List<Student> selectMagor(Connection conn, String input) throws Exception{
		
		List<Student> stdList = new ArrayList<Student>();
		
		try {
			String sql = """
					SELECT STD_NO, STD_NAME, STD_AGE, MAJOR,
					TO_CHAR(ENT_DATE, 'YYYY"년" MM"월" DD"일"') ENT_DATE
					FROM KH_STUDENT
					WHERE MAJOR = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, input);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int stdNo = rs.getInt("STD_NO");
				String stdName = rs.getString("STD_NAME");
				int stdAge = rs.getInt("STD_AGE");
				String major = rs.getString("MAJOR");
				String entDatd = rs.getString("ENT_DATE");
				
				Student std = new Student(stdNo, stdName, stdAge, major, entDatd);
				stdList.add(std);
				
			}
			
			
		} finally {
			close(rs);
			close(pstmt);
		
		}
		
		
		return stdList;
	}

}
