package com.fpt.capstone.wcs.repository;
import com.fpt.capstone.wcs.model.entity.CookieReport;
import com.fpt.capstone.wcs.model.entity.PageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CookieRepository extends JpaRepository<CookieReport,Long> {
    List<CookieReport> findAllByPageOption(PageOption pageOption);
    void removeAllByPageOption(PageOption pageOption);
}
