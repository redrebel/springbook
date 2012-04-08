package springbook.user.service;

import springbook.user.domain.User;

public interface UserService {
	public void upgradeLevels();
	public void add(User user);
}

