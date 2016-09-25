package soselab.easylearn.repository;


import soselab.easylearn.entity.User;

public interface UserRepository {

  public User findById(String id);
  public void save(User user);

}
