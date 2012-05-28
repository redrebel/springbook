package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.sqlservice.SqlService;

public class UserDaoJdbc implements UserDao {
	private JdbcTemplate jdbcTemplate;
	private SqlService sqlService;

	private RowMapper<User> userMapper = 
		new RowMapper<User>() {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getString("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setLevel(Level.valueOf(rs.getInt("level")));
			user.setLogin(rs.getInt("login"));
			user.setRecommend(rs.getInt("recommend"));
			user.setEmail(rs.getString("email"));
			return user;
		}
	};
	
	public void setSqlService(SqlService sqlService){
		this.sqlService = sqlService;
	}
	
	public void setDataSource(DataSource dataSource) {
	this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void add(final User user)throws DuplicateUserIdException{
		try{
			this.jdbcTemplate.update(
					this.sqlService.getSql("add"),
				user.getId(), user.getName(), user.getPassword(),
				user.getLevel().intValue(), user.getLogin(), user.getRecommend(),
				user.getEmail());
		}
		catch(DuplicateKeyException e){
			//throw new DuplicateUserIdException(e);
			throw e;
		}
	}

	public User get(String id){
		return this.jdbcTemplate.queryForObject(
				this.sqlService.getSql("get"),
				new Object[] {id}, this.userMapper);
	}
	
	public void deleteAll(){
		this.jdbcTemplate.update(this.sqlService.getSql("deleteAll"));
	}
	
	public int getCount(){

		return this.jdbcTemplate.queryForInt(this.sqlService.getSql("getCount"));
	}
	
	public List<User> getAll() {
		return this.jdbcTemplate.query(
				this.sqlService.getSql("getAll"), 
				this.userMapper);
	}

	public void update(User user) {
		this.jdbcTemplate.update(
				this.sqlService.getSql("update"), user.getName(), user.getPassword(), user.getEmail(),
				user.getLevel().intValue(), user.getLogin(), user.getRecommend(), 
				user.getId());
		
	}
}


