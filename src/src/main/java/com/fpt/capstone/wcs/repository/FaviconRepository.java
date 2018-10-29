package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.FaviconReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaviconRepository extends JpaRepository<FaviconReport,Long> {
}
