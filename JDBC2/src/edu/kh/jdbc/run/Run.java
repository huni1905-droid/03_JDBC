package edu.kh.jdbc.run;

import edu.kh.jdbc.view.UserView;

public class Run {

	public static void main(String[] args) {

		UserView view = new UserView(); // UserView를 호출하려면 객체 만들어야한다.
		// view.test(); // view라는 객체 안에 
		view.mainMenu();
	
		
	}

}
