package edu.kh.jdbc.view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc.model.dto.User;
import edu.kh.jdbc.model.service.UserService;

// View : 사용자와 직접 상호작용하는 화면(UI)를 담당,
// (사용자에게) 입력을 받고 결과를 출력하는 역할
public class UserView {

	// 필드
	private UserService service = new UserService(); // view 가 service 호출하려면 객체화를 해줘야 한다.
	private Scanner sc = new Scanner(System.in); 
	
	/**
	 * User 관리 프로그램 메인 메뉴 UI (View)
	 */
	public void mainMenu() {
		
		int input = 0; // 메뉴 선택용 변수
		
		do {
			try {
				
				System.out.println("\n===== User 관리 프로그램 =====\n");
				System.out.println("1. User 등록(INSERT)");
				System.out.println("2. User 전체 조회(SELECT)");
				System.out.println("3. User 중 이름에 검색어가 포함된 회원 조회 (SELECT)");
				System.out.println("4. USER_NO를 입력 받아 일치하는 User 조회(SELECT)");
				System.out.println("5. USER_NO를 입력 받아 일치하는 User 삭제(DELETE)");
				System.out.println("6. ID, PW가 일치하는 회원이 있을 경우 이름 수정(UPDATE)");
				System.out.println("7. User 등록(아이디 중복 검사)");
				System.out.println("8. 여러 User 등록하기");
				System.out.println("0. 프로그램 종료");
				
				System.out.print("메뉴 선택 : ");
				input = sc.nextInt();
				sc.nextLine(); // 버퍼에 남은 개행문자 제거
				
				switch (input) {
				case 1: insertUser(); break;
				case 2: selectAll(); break;
				case 3: selectName(); break;
				case 4: selectUser(); break;
				case 5: deleteUser(); break;
				case 6: updateName(); break;
				case 7: insertUser2(); break;
				case 8: multiInsertUser(); break;
				case 0: System.out.println("\n[프로그램 종료]\n"); break;
				default: System.out.println("\n[메뉴 번호만 입력하세요]\n");
				}
				
				System.out.println("\n-------------------------------------\n");
				
				
			} catch ( InputMismatchException e) {
				// Scanner를 이용한 입력 시 자료형이 잘못된 경우
				System.out.println("\n***** 잘못 입력 하셨습니다 *****\n");
				
				input = -1; // 잘못 입력해서 while문 멈추는것 방지
				sc.nextLine(); // 입력 버퍼에 남아있는 잘못된 문자 제거
				
			} catch (Exception e) {
				// 발생되는 예외를 모두 해당 catch 구문으로 모아서 처리
				e.printStackTrace();
			}
			
		} while( input != 0 );

	}

	
	
	
	/** 1. User 등록 관련된 View 
	 * 
	 */
	private void insertUser() throws Exception{
		
		System.out.println("\n==== 1. User 등록 ====\n");
		
		System.out.print("ID : ");
		String userId = sc.next();
		
		System.out.print("PW : ");
		String userPw = sc.next();
		
		System.out.print("Name : ");
		String userName = sc.next();
		
		// 입력받은 값 3개를 한번에 묶어서 전달할 수 있도록
		// User DTO 객체를 생성한 후 필드에 값을 세팅
		User user = new User(); // cf) 기본생성자로 객체 만들면 값 설정하주지 않으면 JVM 기본값으로 들어간다
		// (객체는 Heap 영역에 저장되는데 이때 빈 값이 못 들어가서)
		// setter 이용
		user.setUserID(userId);
		user.setUserPw(userPw);
		user.setUserName(userName);
		
		// 서비스 호출 (INSERT)(우변) 후, 결과 반환(int, 결과행의 갯수)( 좌변 '변수 타입' '결과저장용 변수') 받기
		int result = service.insertUser(user); 
		// service 객체(UserService)에 있는 insertUser() 라는 이름의 메서드를 호출하겠다. 
		// 최종적으로 DB(Service -> DAO 거쳐서)에 정보 전달하기 위한 것이기 때문에
		// 전달인자로 user 전달
		
		
		//----------출력------------
		// 반환된 결과에 따라 출력할 내용 선택
		if(result > 0) {
			System.out.println("\n" + userId + " 사용자가 등록되었습니다.\n");
		}else {
			System.out.println("\n*** 등록 실패 ***\n");
		}
		
		
		
	}
	

	
	
	/** 2. User 전체 조회 관련 View (SELECT) 
	 * 
	 */
	private void selectAll() throws Exception{

		System.out.println("\n===== 2. User 전체 조회 =====\n");
		
		// 서비스 호출(SELECT) 후 결과 반환(List<User>) 받기 
		// (ResultSet의 결과를 한 행씩 받아와서 List에 User 타입으로 한번에 담겠다.)
		List<User> userList = service.selectAll();
		
		// 조회 결과가 없는 경우
		if(userList.isEmpty()) {
			System.out.println("\n**** 조회 결과가 없습니다 ****\n");
			return;
			
		} 
		
		// 조회 결과가 있을 경우
		// userList에 있는 모든 User 객체 출력
		// 향상된 for문 이용!
		for(User user : userList) {
			System.out.println(user);
		}
		
	}

	

	
	/** 3. User 중 이름에 검색어가 포함된 회원 조회
	 * 검색어 입력 : 유
	 * 
	 * 없을때 : "검색 결과 없음" 출력
	 */
	private void selectName() throws Exception{
		
		System.out.println("\n===== 3. User 중 이름에 검색어가 포함된 회원 조회 =====\n");
		System.out.print("검색어 입력 : ");
		String keyword = sc.next();
		
		// 서비스 호출 후 결과 반환받기
		// 데이터가 여러개일 수 있으니깐 List로 받아준다.
		List<User> serchList = service.selectName(keyword);
		// keyword를 DB까지 보내기 위해서 Service에 keyword를 전달해준다.
		
		if(serchList.isEmpty()) {
			System.out.println("검색 결과 없음");
			return;
		}
		
		for(User user : serchList) {
			System.out.println(user);
		}
	}
	
	
	
	
	/** 4. USER_NO를 입력 받아 일치하는 User 조회(SELECT)
	 *   * 1행만 조회되거나 or 일치하는 것 못 찾았거나 -> List로 만들 필요 없다
	 *   
	 *   -- 찾았을때 : USER 객체 출력
	 *   -- 없을 때 : USER_NO가 일치하는 회원 없음 
	 */
	private void selectUser() throws Exception{
		System.out.println("\n===== 4. USER_NO를 입력 받아 일치하는 User 조회 =====\n");
		System.out.print("회원번호 입력 : ");
		int inputNo = sc.nextInt();
		
		// 서비스 호출 후 결과 반환받기
		// USER_NO (PK) == 중복이 있을 수 없다!
		// == 일치하는 사용자가 있다면 딱 1행만 조회된다.
		// -> 1행의 조회 결과를 담기 위해서 User DTO 객체 1개 사용
		User user = service.selectUser(inputNo);
		
		// 조회 결과가 없으면 null, 있으면 null 이 아님
		if(user == null) {
			System.out.println("USER_NO가 일치하는 회원이 없음");
			return;
		}
		
		System.out.println(user);
	}
	
	
	
	
	
	/** 5. USER_NO를 입력받아 일치하는 USER 삭제(DELETE)
	 *  * DML 이다!
	 *  
	 *  -- 삭제 성공했을 떄 : 삭제 성공 출력
	 *  -- 삭제 실패했을 때 : 사용자 번호가 일치하는 User가 존재하지 않음
	 */
	private void deleteUser() throws Exception{
		System.out.println("\n===== 5. USER_NO를 입력받아 일치하는 USER 삭제 =====\n");
		System.out.print("삭제할 사용자 번호 입력 : ");
		int deleteNo = sc.nextInt();
		
		int result = service.deleteUser(deleteNo);
		
		if(result >0) {
			System.out.println("\n***** 삭제 성공 *****\n");
		}else {
			System.out.println("사용자 번호가 일치하는 User 가 존재하지 않음");
			
		}
		
		
	}
	
	
	
	
	
	// ***********깃허브 확인 결과 달라
	/** 6. ID, PW가 일치하는 회원이 있을(SELECT) 경우 이름 수정(UPDATE)
	 * 
	 */
	private void updateName() throws Exception{
		System.out.println("\n=== 6. ID, PW가 일치하는 회원의 이름 수정 ===\n");
		
		System.out.print("ID : ");
		String userId = sc.next();
		
		System.out.print("PW : ");
		String userPw = sc.next();
		
		// 입력받은 ID, PW 가 일치하는 회원이 존재하는지 조회(SELECT)
		// -> 수정할 떄 필요한 데이터 USER_NO 조회해오기
		// (SQL 문에서 UPDATE, DELETE 등을 수행할 때 PK의 값을 조회해야 한다)
		int userNo = service.selectUserNo(userId, userPw);
		
		// 조회 결과가 없을 때
		if(userNo == 0) {
			System.out.println("아이디, 비밀번호가 일치하는 사용자가 없음");
			return;
		}
		
		// 조회 결과 있을 때
		System.out.print("수정할 이름 입력 : ");
		String name = sc.next();
		
		// 위에서 조회된 회원(user)의 이름을 수정
		// 서비스 호출(UPDATE) 후 결과 반환(int) 받기
		int result = service.updateName(name, userNo);
		
		if(result > 0) {
			System.out.println("수정 성공!!!");
		} else {
			System.out.println("수정 실패...");
		}
		
		
	}
	
	
	
	
	
	// ***********깃허브 확인 결과 달라
	/** 7. User 등록(아이디 중복 검사)
	 * 
	 */
	private void insertUser2() throws Exception{
		
		System.out.println("\n=== 7. User 등록(아이디 중복 검사) ===\n");
		
		String userId = null; // 입력된 아이디를 저장할 변수
		
		while(true) {
			
			System.out.println("ID : ");
			userId = sc.next();
			
			// 입력받은 userId가 중복인지 검사하는 
			// 서비스(SELECT) 호출 후 결과(int, 중복 == 1, 아니면 == 0) 반환 받기
			int count = service.idCheck(userId);
			
			if(count == 0) { // 중복이 아닌 경우
				System.out.println("사용 가능한 아니디입니다.");
				break;
			} 
			
			System.out.println("이미 사용중인 아이디 입니다. 다시 입력하세요.");
			
		}
		
		// name, pw 입력 받기
		System.out.print("Name : ");
		String userName = sc.next();
		
		System.out.print("PW : ");
		String userPw = sc.next();
		
		// 입력받은 값 3개를 한번에 묶어서 전달할 수 있도록
		// User DTO 객체를 생성한 후 필드에 값 세팅
		// (1번에서 만든 inserUser 재사용 하기 위해서 User 변수를 보내줘야되서)
		
		User user = new User();
		
		user.setUserID(userId);
		user.setUserPw(userPw);
		user.setUserName(userName);
		
		// 서비스 호출 후 결과 반환 받기
		int result = service.insertUser(user);
		
		if(result > 0) {
			System.out.println("\n" + userId + "사용자가 등록되었습니다.\n");
		}else {
			System.out.println("/n**** /n/");
		}
	}
	
	
	
	
	/** 8. 여러 User 등록하기
	 * 
	 */
	private void multiInsertUser() throws Exception{
		System.out.println("/n=== 8. 여러 User 등록 ===/n");
		
		System.out.print("등록할 User 수 : ");
		int input = sc.nextInt();
		sc.nextLine(); // 버퍼 개행문자 제거 (int 형 다음에 받는 값이 문자열이면 해주는게 좋다)
		
		// 입력받은 회원 정보를 저장할 List 객체 생성
		List<User> userList = new ArrayList<User>();
		
		for(int i = 0; i < input; i++) {
			String userId = null; // 입력된 아이디를 저장할 변수
			
			while(true) {
				
				System.out.print("ID : ");
				userId = sc.next();
				
				// 입력받은 userId가 중복인지 검사하는 
				// 서비스(SELECT) 호출 후 결과(int, 중복 == 1, 아니면 == 0) 반환 받기
				int count = service.idCheck(userId);
				
				if(count == 0) { // 중복이 아닌 경우
					System.out.println("사용 가능한 아니디입니다.");
					break;
				} 
				
				System.out.println("이미 사용중인 아이디 입니다. 다시 입력하세요.");
				
			}
			
			// name, pw 입력 받기
			System.out.print((i+1) + "번째 PW : ");
			String userPw = sc.next();
			
			System.out.print((i + 1) + "번째 Name : ");
			String userName = sc.next();
			
			// 입력받은 값 3개를 한번에 묶어서 전달할 수 있도록
			// User DTO 객체를 생성한 후 필드에 값 세팅
			// (1번에서 만든 inserUser 재사용 하기 위해서 User 변수를 보내줘야되서)
			
			User user = new User();
			
			user.setUserID(userId);
			user.setUserPw(userPw);
			user.setUserName(userName);
			
			// userList에 user 추가
			userList.add(user);
			
		} // for문 종료
		
		// 입력받은 모든 사용자를 insert 하는 서비스 호출
		// -> 결과로 삽입된 행의 개수 반환
		int result = service.multiInsertUser(userList);
		
		if(result == userList.size() /* == input */ ){
			System.out.println("전체 삽입 성공!!!");
			
		}else {
			System.out.println("삽입 실패");
		}
	}
	
	
	
}




