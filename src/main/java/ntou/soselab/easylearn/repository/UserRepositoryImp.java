package ntou.soselab.easylearn.repository;

import java.util.HashMap;

import java.util.Map;

import org.springframework.stereotype.Component;

import ntou.soselab.easylearn.domain.entity.User;
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
