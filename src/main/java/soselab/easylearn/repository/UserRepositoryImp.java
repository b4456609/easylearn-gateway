package soselab.easylearn.repository;

import soselab.easylearn.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserRepositoryImp implements UserRepository {
	
	Map<String, User> store = new HashMap<String, User>();

	@Override
	public User findById(String id) {
		return store.get(id);
	}

	@Override
	public void save(User user) {
		store.put(user.getId(), user);
	}

}
