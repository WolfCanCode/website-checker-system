package com.fpt.capstone.wcs.repository;
import com.fpt.capstone.wcs.model.entity.BrokenPageReport;
import com.fpt.capstone.wcs.model.entity.CookieReport;
import com.fpt.capstone.wcs.model.entity.PageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CookieRepository extends JpaRepository<CookieReport,Long> {
    List<CookieReport> findAllByPageOption(PageOption pageOption);
    CookieReport findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(PageOption pageOption, boolean delFlag);
    List<CookieReport> findAllByPageOptionAndCreatedTime(PageOption pageOption, Date createdTime);
    void removeAllByPageOption(PageOption pageOption);
}
