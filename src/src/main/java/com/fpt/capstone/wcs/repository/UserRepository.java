package com.fpt.capstone.wcs.repository;


import com.fpt.capstone.wcs.model.entity.User;
import com.fpt.capstone.wcs.model.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findOneByEmailAndPasswordAndDelFlagEquals (String email, String password, boolean delFlag);
    User findOneByIdAndTokenAndDelFlagEquals(Long id, String token, boolean delFlag);
    User findOneByIdAndDelFlagEquals(Long id, boolean delFlag);
    User findOneByEmailAndDelFlagEquals(String email, boolean delFlag);
    List<User> findAllByManagerAndDelFlagEqualsAndWebsiteIn(User user, boolean delFlag, List<Website> websites);
    List<User> findAllByManagerAndDelFlagEquals(User user, boolean delFlag);
    User findOneById(Long id);
}
