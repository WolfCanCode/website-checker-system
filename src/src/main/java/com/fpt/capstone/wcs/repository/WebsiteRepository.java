package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebsiteRepository extends JpaRepository<Website,Long> {
}
