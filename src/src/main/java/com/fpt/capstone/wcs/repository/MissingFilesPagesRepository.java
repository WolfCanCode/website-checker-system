package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.MissingFileReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissingFilesPagesRepository extends JpaRepository<MissingFileReport,Long> {
}
