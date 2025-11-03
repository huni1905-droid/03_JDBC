package edu.kh.jdbc.view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import edu.kh.jdbc.model.dto.Student;
import edu.kh.jdbc.model.service.StudentService;

public class StudentView {


		private StudentService service = new StudentService();
		private Scanner sc = new Scanner(System.in);
		// 필드
		

		public void mainMenu() {
			
			int input = 0;
			
			do {
				try {
					
					System.out.println("\n===== User 관리 프로그램 =====\n");
					System.out.println("1. 새로운 학생 정보 등록(INSERT)");
					System.out.println("2. 모든 학생 정보 조회(SELECT)");
					System.out.println("3. 학생 정보(이름, 나이, 전공) 수정");
					System.out.println("4. 한번 기준 학생 정보 삭제(DELETE)");
					System.out.println("5. 전공별 학생 조회");
					System.out.println("0. 프로그램 종료");
					
					System.out.print("메뉴 선택 : ");
					input = sc.nextInt();
					sc.nextLine(); // 버퍼에 남은 개행문자 제거
					
					
					switch (input) {
					case 1: insertStd(); break; 	// 1. 학생 등록
					case 2: selectAll(); break; 	// 2. 전체 학생 조회
					case 3: updateStdIfo(); break; 	// 3. 학생 정보 수정
					case 4: deleteStd(); break;		// 4. 학생 삭제
					case 5: selectMagor(); break; 	// 5. 전공별 조회
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

				
			} while ( input != 0 );
			
			
		
	}


		/** 1. 새로운 학생 정보 등록(INSERT)
		 * 
		 */
		private void insertStd() throws Exception{

			System.out.println("\n===== 학생 정보 등록 =====\n");
			
			System.out.println(" 이름 : ");
			String inputName = sc.next();
			
			System.out.println(" 나이 : ");
			int inputAge = sc.nextInt();
			
			System.out.println(" 전공 : ");
			String inputMgr = sc.next();
			
			Student std = new Student();
			
			std.setStdName(inputName);
			std.setStdAge(inputAge);
			std.setMajor(inputMgr);
		
			int result = service.insertStd(std);
			
			if( result >0 ) {
				System.out.println("\n" + inputName + "학생이 등록되었습니다\n");
			}else {
				System.out.println("\n*** 등록 실패 ***\n");
			}
				
		}


		/** 2. 모든 학생 정보 조회(SELECT)
		 * @throws Exception 
		 * 
		 */
		private void selectAll() throws Exception {

			System.out.println("\n===== 2. 모든 학생 정보 조회 =====\n");
			
			List<Student> stdList = service.selectAll();
			
			if(stdList.isEmpty()) {
				System.out.println("\n**** 조회 결과가 없습니다.\n");
				return;
			}
			
			for(Student std : stdList) {
				System.out.println(std);
			}
			
			
			
			
		}


		/** 3. 학생 정보(이름, 나이, 전공) 수정
		 * 
		 */
		private void updateStdIfo() throws Exception{

			System.out.println("\n===== 3. 학생 정보(이름, 나이, 전공) 수정 =====\n");
			
			System.out.print("학번 : ");
			int inputNo = sc.nextInt();


			System.out.print("이름 : ");
			String inputName = sc.next();
			

			int stdNo = service.selectSdtNo(inputNo, inputName);


			if(stdNo == 0) {

				System.out.println("일치하는 학생이 없습니다");
				return;

			}

			

			System.out.print("수정할 학생의 이름 입력 : ");
			String stdName = sc.next();

			System.out.print("수정할 학생의 나이 입력 : ");
			int stdAge = sc.nextInt();

			System.out.print("수정할 학생의 전공 입력 : ");
			String major = sc.next();
			

			int result = service.updateStd(inputNo, stdName, stdAge, major);
			

			if(result > 0) System.out.println("수정 성공!!!");
			else		   System.out.println("수정 실패...");

		}


		/** 4. 한번 기준 학생 정보 삭제(DELETE)
		 * 
		 */
		private void deleteStd() throws Exception{
			System.out.println("\n===== 4. 한번 기준 학생 정보 삭제(DELETE)\n");
			System.out.print("삭제할 학생 번호 입력 : ");
			int deleteNo = sc.nextInt();
			
			int result = service.deleteStd(deleteNo);
			
			if(result > 0) {
				System.out.println("\n***** 삭제 성공 *****\n");
			} else {
				System.out.println("일치하는 학생 번호가 존재하지 않습니다");
			}
			
		}


		/** 5. 전공별 학생 조회
		 * 
		 */
		private void selectMagor() throws Exception{
			System.out.println("\n===== 5. 전공별 학생 조회 =====\n");	
			System.out.print("조회할 전공 입력 : ");
			String input = sc.next();
			
			List<Student> stdList = service.selectMagor(input);
			
			if(stdList == null) {
				System.out.println("조회할 학생이 없습니다");
				return;
			}
			
			System.out.println(stdList);
			
		}

		
	

}
