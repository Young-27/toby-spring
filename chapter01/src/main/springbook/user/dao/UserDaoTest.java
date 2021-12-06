package main.springbook.user.dao;

import java.sql.SQLException;

/**
 * 1.3.3 관계설정 책임의 분리
 * - 관계설정 책임이 추가된 메소드
 * - 클라이언트 코드
 * - UserDao의 변경 없이 다른 사용자가 자유로운 DB 접속 클래스를 만들어 사용 가능
 */
public class UserDaoTest {

	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		/* 1.3.3
		// UserDao가 사용할 ConnectionMaker 구현 클래스를 결정하고 오브젝트 만들기
		ConnectionMaker connectionMaker = new DConnectionMaker();
		
		// 1. UserDao 생성
		// 2. 사용할 ConnectionMaker 타입의 오브젝트 제공, 결국 두 오브젝트 사이의 의존관계 설정 효과
		UserDao dao = new UserDao(connectionMaker);
		*/
		
		UserDao dao = new DaoFactory().userDao();
	}

}
