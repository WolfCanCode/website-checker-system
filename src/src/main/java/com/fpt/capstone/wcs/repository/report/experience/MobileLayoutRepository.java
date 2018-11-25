package com.fpt.capstone.wcs.repository.report.experience;


import com.fpt.capstone.wcs.model.entity.report.experience.MobileLayoutReport;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MobileLayoutRepository extends JpaRepository<MobileLayoutReport,Long> {
    List<MobileLayoutReport> findAllByPageOption(PageOption pageOption);
    List<MobileLayoutReport> findAllByPageOptionAndUrl(PageOption pageOption, String url);
    MobileLayoutReport findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(PageOption pageOption, boolean delFlag);
    List<MobileLayoutReport> findAllByPageOptionAndCreatedTime(PageOption pageOption, Date createdTime);
    void removeAllByPageOption(PageOption pageOption);
}
