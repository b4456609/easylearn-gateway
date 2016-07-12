package ntou.soselab.easylearn.repository;


import ntou.soselab.easylearn.domain.entity.User;

public interface UserRepository {

  public User findById(String id);
  public void save(User user);

}
