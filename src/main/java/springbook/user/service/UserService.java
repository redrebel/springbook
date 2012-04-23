package springbook.user.service;

import java.util.List;

import springbook.user.domain.User;

public interface UserService {
	public void upgradeLevels();
	User get(String id);
	List<User> getAll();
	void deleteAll();
	public void add(User user);
}

