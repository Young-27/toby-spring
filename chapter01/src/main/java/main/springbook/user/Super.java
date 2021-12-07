package main.springbook.user;


// 템플릿 메소드 패턴
public abstract class Super {
	public void templateMethod() {
		// 기본 알고리즘 코드
		hookMethod();
		abstractMethod();
		/*
		 * 기본 알고리즘 골격을 담은 메소드를 템플릿 메소드라 부른다. 
		 * 템플릿 메소드는 서브클래스에서 오버라이드하거나 구현할 메소드를 사용한다.
		 */
	}
	
	protected void hookMethod() {} 			// 선택적으로 오버라이드 가능한 훅(hook) 메소드
	public abstract void abstractMethod(); // 서브클래스에서 반드시 구현해야 하는 추상 메소드
	
}

/*
public class Sub1 extends Super{ // => 슈퍼클래스의 메소드를 오버라이드하거나 구현해서 기능 확장
	
}
*/
