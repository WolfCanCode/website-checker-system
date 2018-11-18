package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.JavascriptReport;
import com.fpt.capstone.wcs.model.entity.PageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JSCheckRepository extends JpaRepository<JavascriptReport,Long> {
    List<JavascriptReport> findAllByPageOption(PageOption pageOption);
    List<JavascriptReport> findAllByPageOptionAndPages(PageOption pageOption, String url);
    void removeAllByPageOption(PageOption pageOption);
}
