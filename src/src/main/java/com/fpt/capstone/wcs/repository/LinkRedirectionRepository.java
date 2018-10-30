package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.RedirectionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkRedirectionRepository extends JpaRepository<RedirectionReport,Long> {
}
