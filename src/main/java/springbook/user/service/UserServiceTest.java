package springbook.user.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
//import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.mail.MailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECCOMEND_FOR_GOLD;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/test-applicationContext.xml")
public class UserServiceTest {
	@Autowired UserService userService;
	@Autowired UserService testUserService;
	@Autowired UserDao userDao;
	@Autowired DataSource dataSource;
	@Autowired PlatformTransactionManager transactionManager;
	@Autowired MailSender mailSender;
	@Autowired ApplicationContext context;
	
	List<User> users;	// test fixture
	
	@Before
	public void setUp() {
		users = Arrays.asList(
				new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER-1, 0, "user1@ksug.org"),
				new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "user2@ksug.org"),
				new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD-1, "user3@ksug.org"),
				new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD, "user4@ksug.org"),
				new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "user5@ksug.org")
				);
	}
	
	@Test
	@Transactional//(readOnly=true)
	public void transactionSync(){
			userDao.deleteAll();
			userService.add(users.get(0));
			userService.add(users.get(1));
		
	}
	@Test
	public void add() {
		userDao.deleteAll();
		
		User userWithLevel = users.get(4);
		User userWithoutLevel = users.get(0);
		userWithoutLevel.setLevel(null);
		
		userService.add(userWithLevel);
		userService.add(userWithoutLevel);
		
		User userWithLevelRead = userDao.get(userWithLevel.getId());
		User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
		
		assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
		assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
	}
	
	@Test
	@DirtiesContext
	public void upgradeLevels() throws Exception{
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		
		MockUserDao mockUserDao = new MockUserDao(this.users);
		userServiceImpl.setUserDao(mockUserDao);
				
		MockMailSender mockMailSender = new MockMailSender();
		userServiceImpl.setMailSender(mockMailSender);
		
		userServiceImpl.upgradeLevels();
		
		List<User> updated = mockUserDao.getUpdated();
		assertThat(updated.size(), is(2));
		checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
		checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);
		
		List<String> request = mockMailSender.getRequests();
		assertThat(request.size(), is(2));
		assertThat(request.get(0), is(users.get(1).getEmail()));
		assertThat(request.get(1), is(users.get(3).getEmail()));
	}
	
	private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
		assertThat(updated.getId(), is(expectedId));
		assertThat(updated.getLevel(), is(expectedLevel));
	}
	
	@SuppressWarnings("unused")
	private void checkLevel(User user, Level expectedLevel){
		User userUpdate = userDao.get(user.getId());
		assertThat(userUpdate.getLevel(), is(expectedLevel));
	}
	
	private void checkLevelUpgrade(User user, boolean upgraded){
		User userUpdate = userDao.get(user.getId());
		if(upgraded) {
			assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
		}
		else {
			assertThat(userUpdate.getLevel(), is(user.getLevel()));
		}
	}
	
	@Test
	public void upgradeAllOrNothing() throws Exception {
		
		userDao.deleteAll(); 
		for(User user : users) userDao.add(user);
		
		try{
			this.testUserService.upgradeLevels();
			fail("TestUserServiceException expected");
		}
		catch(TestUserServiceException e) {
			
		}
		
		checkLevelUpgrade(users.get(1), false);
	}
	
	@Test//(expected=TransientDataAccessResourceException.class)
	public void readOnlyTransactionAttribute(){
		testUserService.getAll();
	}
	
	static class TestUserServiceImpl extends UserServiceImpl{
		private String id = "madnite1";
		
		protected void upgradeLevel(User user) {
			if(user.getId().equals(this.id)) throw new TestUserServiceException();
			super.upgradeLevel(user);
		}
		
		public List<User> getAll(){
			for(User user:super.getAll()){
				super.update(user);
			}
			return null;
		}
	}
	
	static class TestUserServiceException extends RuntimeException {
		
	}
	
	static class MockUserDao implements UserDao{
		private List<User> users;
		private List<User> updated = new ArrayList<User>();
		
		private MockUserDao(List<User> users) {
			this.users = users;
		}
		
		public List<User> getUpdated(){
			return this.updated;
		}
		
		public List<User> getAll(){
			return this.users;
		}
		
		public void update(User user){
			updated.add(user);
		}
		
		public void add(User user) { throw new UnsupportedOperationException(); }
		public void deleteAll() { throw new UnsupportedOperationException(); }
		public User get(String id) { throw new UnsupportedOperationException(); }
		public int getCount() { throw new UnsupportedOperationException(); }
	}
}
