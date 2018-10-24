package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.JavascriptReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JSCheckRepository extends JpaRepository<JavascriptReport,Long> {
}
