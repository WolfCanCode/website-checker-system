package com.fpt.capstone.wcs.repository.report.content;

import com.fpt.capstone.wcs.model.entity.website.PageOption;
import com.fpt.capstone.wcs.model.entity.report.content.PageReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PageTestRepository extends JpaRepository<PageReport,Long> {
    List<PageReport> findAllByPageOption(PageOption pageOption);
    List<PageReport> findAllByPageOptionAndUrl(PageOption pageOption, String url);
    PageReport findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(PageOption pageOption, boolean delFlag);
    List<PageReport> findAllByPageOptionAndCreatedTime(PageOption pageOption, Date createdTime);
    void removeAllByPageOption(PageOption pageOption);
}
