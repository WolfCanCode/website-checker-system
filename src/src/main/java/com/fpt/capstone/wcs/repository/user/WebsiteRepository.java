package com.fpt.capstone.wcs.repository.user;

import com.fpt.capstone.wcs.model.entity.user.User;
import com.fpt.capstone.wcs.model.entity.user.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebsiteRepository extends JpaRepository<Website,Long> {
    List<Website> findAllByUserAndDelFlagEquals(User user, boolean isDel);
    Website findOneByUserAndIdAndDelFlagEquals(User user, Long id, boolean isDel);
    Website findFirstByUserAndUrlAndDelFlagEqualsOrUserAndNameAndDelFlagEquals(User manager1 , String url, boolean isDel1, User manager2, String name, boolean isDel2);
    Website findOneByUserAndUrlAndDelFlagEquals(User user, String url, boolean isDel);

}
