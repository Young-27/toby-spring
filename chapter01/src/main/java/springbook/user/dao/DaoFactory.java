package springbook.user.dao;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 1.4.1 오브젝트 팩토리
 * - UserDao의 생성 책임을 맡은 팩토리 클래스
 * - UserDaoTest는 이제 UserDao 오브젝트를 받아가기만 하면 된다.
 * 
 * 1.5.1
 * 스프링 빈 팩토리가 사용할 설정정보를 담은 DaoFactory
 */
@Configuration // => 애플리케이션 컨텍스트 또는 빈 팩토리가 사용할 설정정보라는 표시
public class DaoFactory {
	
	@Bean // => 오브젝트 생성을 담당하는 IoC용 메소드라는 표시
	public UserDao userDao() {
		UserDao userDao = new UserDao();
		userDao.setConnectionMaker(connectionMaker()); // 수정자 메소드 DI를 사용
		return userDao;
	}
	@Bean
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
	
	public static void main(String[] args) {
		DaoFactory factory = new DaoFactory();
		UserDao dao1 = factory.userDao();
		UserDao dao2 = factory.userDao();
		
		System.out.println(dao1);
		System.out.println(dao2);
		// => 각기 다른 값을 가진 오브젝트임을 알 수 있다. 
		// => userDao를 호출할 때 마다 새로운 오브젝트 생성
		
		ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		UserDao dao3 = context.getBean("userDao", UserDao.class);
		UserDao dao4 = context.getBean("userDao", UserDao.class);
		
		System.out.println(dao3);
		System.out.println(dao4);
		System.out.println(dao3 == dao4); // true
		// 두 오브젝트의 출력 값이 같다.
		
	}
}
