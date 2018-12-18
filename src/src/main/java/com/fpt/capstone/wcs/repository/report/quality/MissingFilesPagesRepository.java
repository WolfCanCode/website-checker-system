package com.fpt.capstone.wcs.repository.report.quality;

import com.fpt.capstone.wcs.model.entity.report.experience.SpeedTestReport;
import com.fpt.capstone.wcs.model.entity.report.quality.MissingFileReport;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MissingFilesPagesRepository extends JpaRepository<MissingFileReport,Long> {
    List<MissingFileReport> findAllByPageOption(PageOption pageOption);
    List<MissingFileReport> findAllByPageOptionAndPages(PageOption pageOption, String url);
    MissingFileReport findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(PageOption pageOption, boolean delFlag);
    List<MissingFileReport> findAllByPageOptionAndCreatedTime(PageOption pageOption, Date createdTime);
    void removeAllByPageOption(PageOption pageOption);

    @Query(value = "SELECT DISTINCT * " +
            "FROM missing_file_report " +
            "WHERE del_flag = 0 " +
            "GROUP BY created_time  " +
            "ORDER BY created_time DESC " +
            "LIMIT 5", nativeQuery = true)
    List<MissingFileReport> findAllGroupByCreatedTime();
}
