package com.fpt.capstone.wcs.repository.report.experience;


import com.fpt.capstone.wcs.model.entity.report.experience.MobileLayoutReport;
import com.fpt.capstone.wcs.model.entity.report.experience.SpeedTestReport;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MobileLayoutRepository extends JpaRepository<MobileLayoutReport,Long> {
    List<MobileLayoutReport> findAllByPageOption(PageOption pageOption);
    List<MobileLayoutReport> findAllByPageOptionAndUrl(PageOption pageOption, String url);
    MobileLayoutReport findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(PageOption pageOption, boolean delFlag);
    List<MobileLayoutReport> findAllByPageOptionAndCreatedTime(PageOption pageOption, Date createdTime);
    void removeAllByPageOption(PageOption pageOption);

    @Query(value = "SELECT DISTINCT * " +
            "FROM mobile_layout_report " +
            "WHERE del_flag = 0 " +
            "GROUP BY created_time  " +
            "ORDER BY created_time DESC " +
            "LIMIT 5", nativeQuery = true)
    List<MobileLayoutReport> findAllGroupByCreatedTime();
}
