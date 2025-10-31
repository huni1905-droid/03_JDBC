package edu.kh.jdbc.model.service;

import java.sql.Connection;
import java.util.List;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.model.dao.StudentDAO;
import edu.kh.jdbc.model.dto.Student;

public class StudentService {

	StudentDAO dao = new StudentDAO();	
	/** 1. 새로운 학생 정보 등록 서비스
	 * @param std
	 * @return
	 */
	public int insertStd(Student std) throws Exception{

		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.insertStd(conn, std);
		
		if( result >0 ) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}

		JDBCTemplate.close(conn);
		
		return result ;
	}
	
	
	
	/** 2. 모든 학생 정보 조회 서비스
	 * @return
	 */
	public List<Student> selectAll() throws Exception {

		Connection conn = JDBCTemplate.getConnection();
		
		List<Student> stdList = dao.selectAll(conn);
		
		JDBCTemplate.close(conn);
		
		return stdList;
	}



	/** 4. 한번 기준 학생 정보 삭제 서비스
	 * @param deleteNo
	 * @return
	 */
	public int deleteStd(int deleteNo) throws Exception{
		
		Connection conn = JDBCTemplate.getConnection();
		
		int result = dao.deleteStd(conn, deleteNo);
		
		if( result>0 ) {
			JDBCTemplate.commit(conn);
		} else {
			JDBCTemplate.rollback(conn);
		}
			
			
			
		JDBCTemplate.close(conn);
		
		return result;
	}



	/** 5. 전공별 학생 조회 서비스
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public List<Student> selectMagor(String input) throws Exception{
		
		Connection conn = JDBCTemplate.getConnection();
		
		List<Student> stdList = dao.selectMagor(conn, input);
		
		JDBCTemplate.close(conn);
		
		return stdList;
	}

}
