package com.fpt.capstone.wcs.repository.report.content;

import com.fpt.capstone.wcs.model.entity.report.content.ContactReport;
import com.fpt.capstone.wcs.model.entity.report.content.PageReport;
import com.fpt.capstone.wcs.model.entity.report.experience.SpeedTestReport;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ContactDetailRepository extends JpaRepository<ContactReport,Long> {
    List<ContactReport> findAllByPageOption(PageOption pageOption);
    List<ContactReport> findAllByPageOptionAndUrl(PageOption pageOption, String url);
    ContactReport findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(PageOption pageOption, boolean delFlag);
    List<ContactReport> findAllByPageOptionAndCreatedTime(PageOption pageOption, Date createdTime);
    void removeAllByPageOption(PageOption pageOption);

    @Query(value = "SELECT DISTINCT * " +
            "FROM contact_report " +
            "WHERE del_flag = 0 " +
            "GROUP BY created_time " +
            "ORDER BY created_time DESC " +
            "LIMIT 5", nativeQuery = true)
    List<ContactReport> findAllGroupByCreatedTime();
}

