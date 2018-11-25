package com.fpt.capstone.wcs.repository.report.technology;

import com.fpt.capstone.wcs.model.entity.report.technology.FaviconReport;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaviconRepository extends JpaRepository<FaviconReport,Long> {
    List<FaviconReport> findAllByPageOption(PageOption pageOption);
    List<FaviconReport> findAllByPageOptionAndUrl(PageOption pageOption, String url);
    void removeAllByPageOption(PageOption pageOption);
}
