package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.PageOption;
import com.fpt.capstone.wcs.model.entity.PageReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PageTestRepository extends JpaRepository<PageReport,Long> {
    List<PageReport> findAllByPageOption(PageOption pageOption);
    List<PageReport> findAllByPageOptionAndUrl(PageOption pageOption, String url);
    void removeAllByPageOption(PageOption pageOption);
}
