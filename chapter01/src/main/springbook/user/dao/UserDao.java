package main.springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.springbook.user.domain.User;


/**
 * 1.1.2 UserDao
 * JDBC를 이용한 등록과 조회 기능이 있는 UserDao 클래스
 * - mysql 5.1v
 */
public class UserDao {
	
	private ConnectionMaker connectionMaker;
	
	public UserDao() {}
	
	public UserDao(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
		// DConnectionMaker를 생성하는 코드를 UserDao의 클라이언트에게 넘겨버림
	}
	
	public void add(User user) throws ClassNotFoundException, SQLException{ // 예외는 메소드 밖으로 던짐
		
		// DB와 연결
		/*
		 * Class.forName("com.mysql.jdbc.Driver"); Connection c =
		 * DriverManager.getConnection("jdbc:mysql://localhost/springbook", "spring",
		 * "book");
		 */
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
		/*
		 * Class.forName("com.mysql.jdbc.Driver"); Connection c =
		 * DriverManager.getConnection("jdbc:mysql://localhost/springbook", "spring",
		 * "book");
		 */
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
	
	/**
	 * 1.2.2
	 * getConnection() 메소드를 추출해서 중복을 제거
	 */
	private Connection getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost/springbook", "spring", "book");
		return c;
	}
	
	/**
	 * 1.1.3 main()을 이용한 DAO 테스트 코드
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException{
		UserDao dao = new UserDao();
		
		User user = new User();
		user.setId("whiteship");
		user.setName("백기선");
		user.setPassword("married");
		
		dao.add(user);
		
		System.out.println(user.getId() + " 등록 성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		
		System.out.println(user2.getId() + " 조회 성공");
	}
}
