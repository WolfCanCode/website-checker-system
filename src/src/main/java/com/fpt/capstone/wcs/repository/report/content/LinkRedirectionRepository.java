package com.fpt.capstone.wcs.repository.report.content;

import com.fpt.capstone.wcs.model.entity.website.PageOption;
import com.fpt.capstone.wcs.model.entity.report.content.RedirectionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LinkRedirectionRepository extends JpaRepository<RedirectionReport,Long> {
    List<RedirectionReport> findAllByPageOption(PageOption pageOption);
    List<RedirectionReport> findAllByPageOptionAndUrl(PageOption pageOption, String url);
    RedirectionReport findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(PageOption pageOption, boolean delFlag);
    List<RedirectionReport> findAllByPageOptionAndCreatedTime(PageOption pageOption, Date createdTime);
    void removeAllByPageOption(PageOption pageOption);
}
