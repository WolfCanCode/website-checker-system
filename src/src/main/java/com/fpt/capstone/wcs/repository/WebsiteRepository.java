package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.User;
import com.fpt.capstone.wcs.model.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebsiteRepository extends JpaRepository<Website,Long> {
    List<Website> findAllByUser(User user);
    Website findOneByUserAndId(User user, Long id);
}
