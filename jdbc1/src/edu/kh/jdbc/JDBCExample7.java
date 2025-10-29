package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class JDBCExample7 {

	public static void main(String[] args) {

		// EMPLOYEE	테이블에서
		// 사번, 이름, 성별, 급여, 직급명, 부서명을 조회
		// 단, 입력 받은 조건에 맞는 결과만 조회하고 정렬할 것
						
		// - 조건 1 : 성별 (M, F)
		// - 조건 2 : 급여 범위
		// - 조건 3 : 급여 오름차순/내림차순
						
		// [실행화면]
		// 조회할 성별(M/F) : F
		// 급여 범위(최소, 최대 순서로 작성) :
		// 3000000
		// 4000000
		// 급여 정렬(1.ASC, 2.DESC) : 2
						
		// 사번 | 이름   | 성별 | 급여    | 직급명 | 부서명
		//--------------------------------------------------------
		// 217  | 전지연 | F    | 3660000 | 대리   | 인사관리부
		// -------------------------------------------------------
				
		// 사번 | 이름   | 성별 | 급여    | 직급명 | 부서명
		//--------------------------------------------------------
		// 218  | 이오리 | F    | 3890000 | 사원   | 없음
		// 203  | 송은희 | F    | 3800000 | 차장   | 해외영업2부
		// 212  | 장쯔위 | F    | 3550000 | 대리   | 기술지원부
		// 222  | 이태림 | F    | 3436240 | 대리   | 기술지원부
		// 207  | 하이유 | F    | 3200000 | 과장   | 해외영업1부
		// 210  | 윤은해 | F    | 3000000 | 사원   | 해외영업1부

		
		// 1. JDBC 객체 참조변수 선언
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		Scanner sc = null;
		
		try {
			
			// 2. DriverMananger 를 이용해서 Connection 생성
			Class.forName("oracle.jdbc.driver.OracleDriver");

			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String userName = "kh_kjh";
			String userPass = "kh1234";
			
			conn = DriverManager.getConnection(url, userName, userPass);
			System.out.println(conn);
			
			
			// 3. SQL 작성
			sc = new Scanner(System.in); 
			
			System.out.print("조회할 성별(M/F) : ");
			String gender = sc.next().toUpperCase(); // 소문자 -> 대문자 변환
			
			System.out.print("급여 범위(최소, 최대 순서로 작성) :");
			int minSal = sc.nextInt();
			int maxSal = sc.nextInt();
			
			System.out.print("급여 정렬(1.ASC, 2.DESC) : ");
			int orderinput = sc.nextInt();
			
			
			String sql = """
					SELECT EMP_ID, EMP_NAME, DECODE(SUBSTR(EMP_NO, 8, 1), '1', 'M', '2', 'F') GENDER, SALARY, JOB_NAME, NVL(DEPT_TITLE, '없음')
					FROM EMPLOYEE
					JOIN JOB USING(JOB_CODE)
					LEFT JOIN DEPARTMENT ON(DEPT_CODE = DEPT_ID)
					WHERE DECODE(SUBSTR(EMP_NO, 8, 1), '1', 'M', '2', 'F') = ?
					AND SALARY BETWEEN ? AND ?
					ORDER BY SALARY 
					""";
			// ORDER BY 절에 ? (위치홀더) 사용 시 오류 : SQL 명령어가 올바르게 종료되지 않았습니다
			// 왜? 
			// PreparedStatement의 위치홀더(?)는 *** 데이터 값(리터럴) *** 을 대체하는 용도로만 사용가능.
			// -> SQL에서 ORDER BY 절의 정렬 기준 (ASC/DESC)과 같은
			// -> SQL 구문(문법)의 일부는 PreparedStatement의 위치 홀더(?)로 대체될 수 없음
			if(orderinput == 1) sql += " ASC";
			else				sql += " DESC";
			
			
			// 4. PreparedStatement 객체 생성	
			pstmt = conn.prepareStatement(sql);
			
			
			// 5. ? 위치홀더에 알맞은 값 세팅
			pstmt.setString(1, gender);
			pstmt.setInt(2, minSal);
			pstmt.setInt(3, maxSal);

			
			// 6. SQL 수행 후 결과 반환 받기
			rs = pstmt.executeQuery();

			
			// 7. 커서를 이용해서 한 행씩 접근하여 컬럼 값 얻어와 출력하기
			boolean flag = true; // true : 조회결과가 없음, false : 조회결과 존재함!
			
			System.out.println("사번 | 이름 | 성별 | 급여 | 직급명 | 부서명");
			System.out.println("---------------------------------------------");
			
			while(rs.next()) {
				String empId 	= rs.getString("EMP_ID");
				String empName 	= rs.getString("EMP_NAME");
				String gen 		= rs.getString("GENDER");
				int salary 		= rs.getInt("SALARY");
				String jobName= rs.getString("JOB_NAME");
				String deptTitle = rs.getString("DEPT_TITLE");
				
				System.out.printf("%-4s | %3s | %-4s | %7d | %-3s  | %s \n",
						empId, empName, gen, salary, jobName, deptTitle);
				
				flag = false;
				// while 문이 1회 이상 반복됨 == 조회 결과가 1행이라도 있다
			}
			
			if(flag){ // flag == true인 경우 -> while문 아쪽 수행X -> 조회결과가 1행도 없음
				System.out.println("조회결과 없음");
				
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
				
				if(sc != null) sc.close();

				
			} catch (Exception e) {
				e.printStackTrace();
			
			}
		}
		
		
		
		
		
	}

}
