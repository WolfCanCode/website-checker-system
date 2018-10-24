package com.fpt.capstone.wcs.repository;
import com.fpt.capstone.wcs.model.entity.CookieReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CookieRepository extends JpaRepository<CookieReport,Long> {
}
