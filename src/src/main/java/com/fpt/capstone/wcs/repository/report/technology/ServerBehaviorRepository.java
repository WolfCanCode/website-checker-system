package com.fpt.capstone.wcs.repository.report.technology;

import com.fpt.capstone.wcs.model.entity.report.experience.SpeedTestReport;
import com.fpt.capstone.wcs.model.entity.report.technology.ServerBehaviorReport;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ServerBehaviorRepository extends JpaRepository<ServerBehaviorReport, Long> {
    void removeAllByPageOption(PageOption pageOption);

    List<ServerBehaviorReport> findAllByPageOptionAndDelFlagEquals(PageOption pageOption, boolean delFlag);

    ServerBehaviorReport findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(PageOption pageOption, boolean delFlag);

    List<ServerBehaviorReport> findAllByPageOptionAndCreatedTime(PageOption pageOption, Date time);

    @Query(value = "SELECT DISTINCT * " +
            "FROM server_behavior_report " +
            "WHERE del_flag = 0 " +
            "GROUP BY created_time  " +
            "ORDER BY created_time DESC " +
            "LIMIT 5", nativeQuery = true)
    List<ServerBehaviorReport> findAllGroupByCreatedTime();
}
