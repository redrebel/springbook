package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import springbook.user.domain.User;

public class UserDao {
	private DataSource dataSource;
	private ConnectionMaker connectionMaker;
	
	//public UserDao(ConnectionMaker connectionMaker) {
	//	this.connectionMaker = connectionMaker;
	//}


	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setConnectionMaker(ConnectionMaker connectionMaker) {
		this.connectionMaker = connectionMaker;
	}

	public void jdbcContextWithStatementStrategy(StatementStratery stmt) throws
		SQLException{
		Connection c = null;
		PreparedStatement ps =null;
		
		try{
			c = dataSource.getConnection();
			ps = stmt.makePreparedStatement(c);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}finally {
			if(ps != null) { try { ps.close(); } catch (SQLException e) {} }
			if(c != null) { try { c.close(); } catch (SQLException e) {} }
		}
	}
	
	public void add(final User user) throws ClassNotFoundException, SQLException{
		//Connection c = connectionMaker.makeConnection();
		/*
		Connection c = dataSource.getConnection();
		
		PreparedStatement ps = c.prepareStatement(
				"insert into users(id, name, password) values(?,?,?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());
		
		ps.executeUpdate();
		
		ps.close();
		c.close();
		*/
		
		jdbcContextWithStatementStrategy(
			new StatementStratery() {
				public PreparedStatement makePreparedStatement(Connection c)
						throws SQLException {
					// TODO Auto-generated method stub
					PreparedStatement ps = 
						c.prepareStatement("insert into users(id, name, password) values(?,?,?)");
					ps.setString(1, user.getId());
					ps.setString(2, user.getName());
					ps.setString(3, user.getPassword());
						
					return ps;
				}
			}
		);
	}

	public User get(String id) throws ClassNotFoundException, SQLException{
		//Connection c = connectionMaker.makeConnection();
		Connection c = dataSource.getConnection();
		PreparedStatement ps = c.prepareStatement(
				"select * from users where id = ?");
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
	
	public void deleteAll() throws SQLException{
		/*
		Connection c = null;
		PreparedStatement ps = null; 
			
		try{
			c = dataSource.getConnection();
			ps = c.prepareStatement("delete from users");
			ps.executeUpdate();
		} catch (SQLException e){
			throw e;
		} finally {
			if(ps != null ){
				try {
					ps.close();
				} catch (SQLException e) {
				}
			}
			if(c != null){
				try {
					c.close();
				} catch (SQLException e){
				}
			}
		}
	*/
		jdbcContextWithStatementStrategy(
			new StatementStratery() {
				public PreparedStatement makePreparedStatement(Connection c)
					throws SQLException {
					// TODO Auto-generated method stub
					
					return c.prepareStatement("delete from users");
				}
			}	
		);
	}
	
	public int getCount() throws SQLException {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			c = dataSource.getConnection();
			ps = c.prepareStatement("select count(*) from users");
			
			rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			throw e;
		} finally {
			if(rs != null) {
				try{
					rs.close();
				} catch (SQLException e){
					
				}
			}
			if(ps != null) {
				try{
					ps.close();
				} catch (SQLException e) {
					
				}
			}
			if(c != null) {
				try{
					c.close();
				} catch (SQLException e) {
					
				}
			}
		}
	}
}


