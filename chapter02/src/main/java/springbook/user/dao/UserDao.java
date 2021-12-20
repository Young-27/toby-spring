package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;

import springbook.user.domain.User;

/**
 * 1.1.2 UserDao JDBC를 이용한 등록과 조회 기능이 있는 UserDao 클래스 - mysql 5.1v
 */
public class UserDao {

	private DataSource dataSource;
	
	@Bean
	public DataSource dataSource() {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
		dataSource.setUrl("jdbc:mysql://localhost/springbook");
		dataSource.setUsername("spring");
		dataSource.setPassword("book");
		
		return dataSource;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void add(User user) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement ps = c.prepareStatement("insert into user(id, name, password) value(?, ?, ?)");

		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());

		ps.executeUpdate();

		ps.close();
		c.close();
	}

	private ConnectionMaker connectionMaker;
	private Connection c;
	private User user;
	
	public UserDao(){}
	
	public UserDao userDao() {
		UserDao userDao = new UserDao();
		userDao.setDataSource(dataSource());
		return userDao;
	}

	public UserDao(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
		// DConnectionMaker를 생성하는 코드를 UserDao의 클라이언트에게 넘겨버림
	}

	// 싱글톤 패턴 적용 -> 자신과 같은 타입의 스태틱 필드를 정의
	private static UserDao INSTANCE;

	public static synchronized UserDao getInstance() {
		if (INSTANCE == null)
			INSTANCE = new UserDao(/* connectionMaker */);
		return INSTANCE;
	}
	// 결과 => 코드가 지저분해짐, private 생성자를 외부에서 호출할 수 없기 때문에 ConnectionMaker 오브젝트 넣어주기 불가능

	// setter 메소드
	public void setConnectionMaker(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
	}


	
	  // 인스턴스 변수를 사용하도록 수정 
		public User get(String id) throws ClassNotFoundException, SQLException {
			this.c = connectionMaker.makeConnection();

			PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
			ps.setString(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();

			this.user = new User();
			this.user.setId(rs.getString("id"));
			this.user.setName(rs.getString("name"));
			this.user.setPassword(rs.getString("password"));

			rs.close();
			ps.close();
			c.close();

			return user;
		}
	  
	  /*
	  public void add(User user) throws ClassNotFoundException, SQLException{ //
	  예외는 메소드 밖으로 던짐
	  
	  // DB와 연결 /* Class.forName("com.mysql.jdbc.Driver"); Connection c =
	  DriverManager.getConnection("jdbc:mysql://localhost/springbook", "spring",
	  "book");
	 /*
		 * Connection c = getConnection(); PreparedStatement ps =
		 * c.prepareStatement("insert into user(id, name, password) value(?, ?, ?)");
		 * 
		 * ps.setString(1, user.getId()); ps.setString(2, user.getName());
		 * ps.setString(3, user.getPassword());
		 * 
		 * ps.executeUpdate();
		 * 
		 * ps.close(); c.close(); }
		 * 
		 * 
		 * /* public User get(String id) throws ClassNotFoundException, SQLException{ /*
		 * Class.forName("com.mysql.jdbc.Driver"); Connection c =
		 * DriverManager.getConnection("jdbc:mysql://localhost/springbook", "spring",
		 * "book");
		 *//*
			 * Connection c = getConnection();
			 * 
			 * PreparedStatement ps =
			 * c.prepareStatement("select * from users where id = ?"); ps.setString(1, id);
			 * 
			 * ResultSet rs = ps.executeQuery(); rs.next(); User user = new User();
			 * user.setId(rs.getString("id")); user.setName(rs.getString("name"));
			 * user.setPassword(rs.getString("password"));
			 * 
			 * rs.close(); ps.close(); c.close();
			 * 
			 * return user; }
			 */

	/**
	 * 1.2.2 getConnection() 메소드를 추출해서 중복을 제거
	 */
	
	private Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost/springbook", "spring", "book");
		return c;
	}
	  
	 /**
		 * 1.1.3 main()을 이용한 DAO 테스트 코드
		 */
		public static void main(String[] args) throws ClassNotFoundException, SQLException {
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
