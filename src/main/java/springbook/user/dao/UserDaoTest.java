package springbook.user.dao;

import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.JUnitCore;

import static org.hamcrest.CoreMatchers.is;	// 직접넣어주어야됨
import static org.junit.Assert.assertThat;	// 직접넣어주어야됨
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;


import springbook.user.domain.*;
import springbook.user.dao.*;

public class UserDaoTest {
	@Test
	public void count() throws ClassNotFoundException, SQLException {
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		
		UserDao dao = context.getBean("userDao", UserDao.class);
		User user1 = new User("gyumee", "박성철", "springno1");
		User user2 = new User("leegw700", "이길원", "springno2");
		User user3 = new User("bumjin", "박범진", "springno3");
		
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));
		
		dao.add(user1);
		assertThat(dao.getCount(), is(1));
		
		dao.add(user2);
		assertThat(dao.getCount(), is(2));
		
		dao.add(user3);
		assertThat(dao.getCount(), is(3));
	}
	
	@Test
	public void addAndGet() throws ClassNotFoundException, SQLException {
		
		//ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
		ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
		UserDao dao = context.getBean("userDao", UserDao.class);
		
		
		//UserDao dao = new DaoFactory().userDao();
		dao.deleteAll();
		assertThat(dao.getCount(), is(0));

		User user = new User();
		//user.setId("whiteship");
		//user.setName("백기선");
		//user.setPassword("married");
		user.setId("gyumee");
		user.setName("박성철");
		user.setPassword("springno1");
		
		dao.add(user);
		assertThat(dao.getCount(), is(1));
		User user2 = dao.get(user.getId());
		
		assertThat(user2.getName(), is(user.getName()));
		assertThat(user2.getPassword(), is(user.getPassword()));
		/*
		System.out.println(user.getId() + " 등록 성공");
		
		User user2 = dao.get(user.getId());
		System.out.println(user2.getName());
		System.out.println(user2.getPassword());
		
		System.out.println(user2.getId() + " 조회 성공");
		System.out.println(dao.getCount());
		*/
	}
	
	

}
