package com.fpt.capstone.wcs.repository;


import com.fpt.capstone.wcs.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findOneByEmailAndPassword (String email, String password);
    User findOneByIdAndToken(Long id, String token);
    List<User> findAllByManager(User user);
    List<User> findAllByManager_Id(long id);
}
