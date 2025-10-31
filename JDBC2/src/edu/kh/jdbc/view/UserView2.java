package edu.kh.jdbc.view;

import java.util.InputMismatchException;
import java.util.Scanner;

import edu.kh.jdbc.model.dto.User;
import edu.kh.jdbc.model2.service.UserService2;

// View : 사용자와 직접 상호작용하는 화면(UI)를 담당,
// (사용자에게) 입력을 받고 결과를 출력하는 역할
public class UserView2 {

	// 필드
	private UserService2 service = new UserService2(); // view 가 service 호출하려면 객체화를 해줘야 한다.
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

	
	
	
	private void insertUser() throws Exception {
		System.out.println("\n ===== 1. User 등록(INSERT) =====\n");
		
		System.out.print(" ID : ");
		String userId = sc.next();
		
		System.out.print(" PW : ");
		String userPw = sc.next();
		
		System.out.print(" Name : ");
		String userName = sc.next();
		
		User user = new User();
		
		user.setUserID(userId);
		user.setUserPw(userPw);
		user.setUserName(userName);
		
		int result = service.insertUser(user);
		
		
		// 입력받은 값 3개를 한번에 묶어서 전달할 수 있도록
		// User DTO 객체를 생성한 후 필드에 값을 세팅
		 // cf) 기본생성자로 객체 만들면 값 설정하주지 않으면 JVM 기본값으로 들어간다
								// (객체는 Heap 영역에 저장되는데 이때 빈 값이 못 들어가서)
		// setter 이용
	
		
		// 서비스 호출 (INSERT) 후 결과 반환(int, 결과행의 갯수) 받기
		 //필드에 생성해 놓은 service 객체
		// service 객체(UserService)에 있는 insertUser() 라는 이름의 메서드를 호출하겠다. 
		// 최종적으로 DB(Service -> DAO 거쳐서)에 정보 전달하기 위한 것이기 때문에
		// 전달인자로 user 전달
		
		
		//----------출력------------
		// 반환된 결과에 따라 출력할 내용 선택
	
		
		
	
	
		
		
		
		
		
		
		
		
		
		
	}

	private void selectAll() {
		// TODO Auto-generated method stub
		
	}

	private void selectName() {
		// TODO Auto-generated method stub
		
	}

	private void selectUser() {
		// TODO Auto-generated method stub
		
	}

	private void deleteUser() {
		// TODO Auto-generated method stub
		
	}

	private void updateName() {
		// TODO Auto-generated method stub
		
	}

	private void insertUser2() {
		// TODO Auto-generated method stub
		
	}

	private void multiInsertUser() {
		// TODO Auto-generated method stub
		
	}
}
