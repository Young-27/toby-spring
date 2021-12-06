package main.springbook.user.dao;

/**
 * 1.4.1 오브젝트 팩토리
 * - UserDao의 생성 책임을 맡은 팩토리 클래스
 * - UserDaoTest는 이제 UserDao 오브젝트를 받아가기만 하면 된다.
 */
public class DaoFactory {
	public UserDao userDao() {
		ConnectionMaker connectionMaker = new DConnectionMaker();
		UserDao userDao = new UserDao(connectionMaker);
		return userDao;
	}
}
