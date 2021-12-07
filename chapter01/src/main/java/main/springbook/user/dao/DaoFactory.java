package main.springbook.user.dao;

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
		ConnectionMaker connectionMaker = new DConnectionMaker();
		UserDao userDao = new UserDao(connectionMaker);
		return userDao;
	}
	@Bean
	public ConnectionMaker connectionMaker() {
		return new DConnectionMaker();
	}
}
