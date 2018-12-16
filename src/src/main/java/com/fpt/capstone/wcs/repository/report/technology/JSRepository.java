package com.fpt.capstone.wcs.repository.report.technology;

import com.fpt.capstone.wcs.model.entity.report.technology.JavascriptReport;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface JSRepository extends JpaRepository<JavascriptReport,Long> {
    List<JavascriptReport> findAllByPageOptionAndCreatedTime(PageOption pageOption, Date time);
    List<JavascriptReport> findAllByPageOptionAndPages(PageOption pageOption, String url);
    JavascriptReport findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(PageOption pageOption, boolean delFlag);
    void removeAllByPageOption(PageOption pageOption);
}
