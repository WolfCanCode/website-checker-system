package com.fpt.capstone.wcs.repository.report.experience;


import com.fpt.capstone.wcs.model.entity.website.PageOption;
import com.fpt.capstone.wcs.model.entity.report.experience.SpeedTestReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SpeedtestRepository extends JpaRepository<SpeedTestReport,Long> {
    List<SpeedTestReport> findAllByPageOption(PageOption pageOption);
    List<SpeedTestReport> findAllByPageOptionAndUrl(PageOption pageOption, String url);
    SpeedTestReport findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(PageOption pageOption,boolean delFlag);
    List<SpeedTestReport> findAllByPageOptionAndCreatedTime(PageOption pageOption, Date createdTime);
    void removeAllByPageOption(PageOption pageOption);

    @Query(value = "SELECT DISTINCT * " +
            "FROM speed_test_report " +
            "WHERE del_flag = 0 " +
            "GROUP BY created_time  " +
            "ORDER BY created_time DESC " +
            "LIMIT 5", nativeQuery = true)
    List<SpeedTestReport> findAllGroupByCreatedTime();
}
