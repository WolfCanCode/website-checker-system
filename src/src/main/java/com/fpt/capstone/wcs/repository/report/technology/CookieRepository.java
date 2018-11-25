package com.fpt.capstone.wcs.repository.report.technology;
import com.fpt.capstone.wcs.model.entity.report.technology.CookieReport;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import com.fpt.capstone.wcs.model.entity.report.quality.BrokenPageReport;
import com.fpt.capstone.wcs.model.entity.report.technology.CookieReport;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
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
