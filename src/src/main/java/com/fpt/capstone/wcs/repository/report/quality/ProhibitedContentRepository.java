package com.fpt.capstone.wcs.repository.report.quality;
import com.fpt.capstone.wcs.model.entity.report.experience.SpeedTestReport;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import com.fpt.capstone.wcs.model.entity.report.quality.ProhibitedContentReport;
import com.fpt.capstone.wcs.model.entity.report.quality.BrokenLinkReport;
import com.fpt.capstone.wcs.model.entity.report.technology.CookieReport;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import com.fpt.capstone.wcs.model.entity.report.quality.ProhibitedContentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository

public interface ProhibitedContentRepository extends JpaRepository<ProhibitedContentReport,Long> {
    List<ProhibitedContentReport> findAllByPageOption(PageOption pageOption);
    List<ProhibitedContentReport> findAllByPageOptionAndUrlPage(PageOption pageOption, String url);
    ProhibitedContentReport findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(PageOption pageOption, boolean delFlag);
    List<ProhibitedContentReport> findAllByPageOptionAndCreatedTime(PageOption pageOption, Date createdTime);
    void removeAllByPageOption(PageOption pageOption);

    @Query(value = "SELECT DISTINCT * " +
            "FROM prohibited_content_report " +
            "WHERE del_flag = 0 " +
            "GROUP BY created_time  " +
            "ORDER BY created_time DESC " +
            "LIMIT 5", nativeQuery = true)
    List<ProhibitedContentReport> findAllGroupByCreatedTime();
}