package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends JpaRepository<Page,Long> {
}
