package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.BrokenLinkReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrokenLinkRepository extends JpaRepository<BrokenLinkReport,Long> {
}
