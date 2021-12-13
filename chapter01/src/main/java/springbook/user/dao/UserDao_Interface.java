package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import springbook.user.domain.User;

/**
 * 1.3.2 인터페이스의 도입
 * ConnectionMaker 인터페이스를 사용하도록 개선한 UserDao
 */
public class UserDao_Interface {
	
	private ConnectionMaker connectionMaker;
	// => 인터페이스를 통해 오브젝트에 접근하므로 구체적인 클래스 정보를 알 필요 없음
	
	public UserDao_Interface() {
		connectionMaker = new DConnectionMaker(); // **클래스 이름 등장 => 또 다른 문제 등장
	}
	
	public void add(User user) throws ClassNotFoundException, SQLException{ // 예외는 메소드 밖으로 던짐
		
		// 인터페이스에 정의된 메소드를 사용하기 때문에 메소드 이름이 변경될 걱정 없음
		Connection c = connectionMaker.makeConnection();
		
		
		PreparedStatement ps = c.prepareStatement("insert into user(id, name, password) value(?, ?, ?)");
		
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
	}
	
	public User get(String id) throws ClassNotFoundException, SQLException{
		Connection c = connectionMaker.makeConnection();
		
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
	
}
