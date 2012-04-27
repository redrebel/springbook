package springbook.user.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import springbook.user.domain.User;

@Transactional
public interface UserService {
	public void upgradeLevels();
	User get(String id);
	List<User> getAll();
	void deleteAll();
	public void add(User user);
}

