package com.fpt.capstone.wcs.repository.report.technology;

import com.fpt.capstone.wcs.model.entity.report.experience.SpeedTestReport;
import com.fpt.capstone.wcs.model.entity.report.technology.FaviconReport;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FaviconRepository extends JpaRepository<FaviconReport,Long> {
    List<FaviconReport> findAllByPageOption(PageOption pageOption);
    List<FaviconReport> findAllByPageOptionAndUrl(PageOption pageOption, String url);
    FaviconReport findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(PageOption pageOption, boolean delFlag);
    List<FaviconReport> findAllByPageOptionAndCreatedTime(PageOption pageOption, Date createdTime);
    void removeAllByPageOption(PageOption pageOption);

    @Query(value = "SELECT DISTINCT * " +
            "FROM favicon_report " +
            "WHERE del_flag = 0 " +
            "GROUP BY created_time  " +
            "ORDER BY created_time DESC " +
            "LIMIT 5", nativeQuery = true)
    List<FaviconReport> findAllGroupByCreatedTime();
}
