package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.BrokenPageReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrokenPageRepository extends JpaRepository<BrokenPageReport,Long> {
}
