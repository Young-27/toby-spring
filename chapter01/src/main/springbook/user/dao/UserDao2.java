package main.springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.springbook.user.domain.User;

/**
 *  1.2.3 DB 컨넥션 만들기 독립
 *  - 상속을 통한 확장
 */
public abstract class UserDao2 {
	// *DAO의 핵심 기능인 어떻게 데이터를 등록하고 가져올 것인가 ==> UserDao
	public void add(User user) throws ClassNotFoundException, SQLException{ // 예외는 메소드 밖으로 던짐
		Connection c = getConnection();
		PreparedStatement ps = c.prepareStatement("insert into user(id, name, password) value(?, ?, ?)");
		
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}
	
	public User get(String id) throws ClassNotFoundException, SQLException{
		Connection c = getConnection();
		
		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
		ps.setString(1, id);
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		User user = new User();
		user.setId(rs.getString("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));
		
		rs.close();
		ps.close();
		c.close();
		
		return user;
	}
	
	// 구현 코드 제거, 메소드의 구현은 서브클래스가 담당
	public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
	
	// *DB 연결 방법은 어떻게 할 것인가 ==> NUserDao, DUserDao
	public class NUserDao extends UserDao{
		public Connection getConnection() throws ClassNotFoundException, SQLException{ // 상속을 통해 확장된 getConnection() 메소드
			return null; // N사 DB connection 생성코드
		}
	}
	
	public class DUserDao extends UserDao{
		public Connection getConnection() throws ClassNotFoundException, SQLException{
			return null; // D사 DB connection 생성코드
		}
	}
	
	
}
