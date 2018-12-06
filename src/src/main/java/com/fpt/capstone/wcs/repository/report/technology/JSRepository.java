package com.fpt.capstone.wcs.repository.report.technology;

import com.fpt.capstone.wcs.model.entity.report.technology.JavascriptReport;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JSRepository extends JpaRepository<JavascriptReport,Long> {
    List<JavascriptReport> findAllByPageOptionAndDelFlagEquals(PageOption pageOption, boolean delFlag);
    List<JavascriptReport> findAllByPageOptionAndPages(PageOption pageOption, String url);
    void removeAllByPageOption(PageOption pageOption);
}
