package com.fpt.capstone.wcs.repository.report.quality;

import com.fpt.capstone.wcs.model.entity.report.experience.SpeedTestReport;
import com.fpt.capstone.wcs.model.entity.report.quality.BrokenLinkReport;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BrokenLinkRepository extends JpaRepository<BrokenLinkReport,Long> {
    List<BrokenLinkReport> findAllByPageOption(PageOption pageOption);
    List<BrokenLinkReport> findAllByPageOptionAndUrlLink(PageOption pageOption, String url);
    BrokenLinkReport findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(PageOption pageOption, boolean delFlag);
    List<BrokenLinkReport> findAllByPageOptionAndCreatedTime(PageOption pageOption, Date createdTime);

    void removeAllByPageOption(PageOption pageOption);

    @Query(value = "SELECT DISTINCT * " +
            "FROM broken_link_report a, page_option p, website w, user u, website_user wu " +
            "WHERE a.del_flag = 0 " +
            "AND a.page_option_id = p.id " +
            "AND p.website_id = w.id " +
            "AND w.id = wu.web_id " +
            "AND u.id = wu.user_id " +
            "AND u.id = :id " +
            "GROUP BY a.created_time " +
            "ORDER BY a.created_time DESC " +
            "LIMIT 5", nativeQuery = true)
    List<BrokenLinkReport> findAllGroupByCreatedTime(@Param("id") long id);

    @Query(value = "SELECT DISTINCT * " +
            "FROM broken_link_report a, page_option p, website w, user u, website_user wu " +
            "WHERE a.del_flag = 0 " +
            "AND a.page_option_id = p.id " +
            "AND p.website_id = w.id " +
            "AND w.id = wu.web_id " +
            "AND u.id = wu.user_id " +
            "AND u.id = :id " +
            "AND p.id = :pageId " +
            "GROUP BY a.created_time " +
            "ORDER BY a.created_time DESC " +
            "LIMIT 5", nativeQuery = true)
    List<BrokenLinkReport> findAllGroupByCreatedTimeAndPageOption(@Param("id") long id, @Param("pageId") long pageId);

    List<BrokenLinkReport> findALlByCreatedTime(Date time);
}
