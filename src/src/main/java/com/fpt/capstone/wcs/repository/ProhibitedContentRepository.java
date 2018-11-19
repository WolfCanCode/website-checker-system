package com.fpt.capstone.wcs.repository;
import com.fpt.capstone.wcs.model.entity.BrokenLinkReport;
import com.fpt.capstone.wcs.model.entity.PageOption;
import com.fpt.capstone.wcs.model.entity.ProhibitedContentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ProhibitedContentRepository extends JpaRepository<ProhibitedContentReport,Long> {
    List<ProhibitedContentReport> findAllByPageOption(PageOption pageOption);
    List<ProhibitedContentReport> findAllByPageOptionAndUrlPage(PageOption pageOption, String url);
    void removeAllByPageOption(PageOption pageOption);
}