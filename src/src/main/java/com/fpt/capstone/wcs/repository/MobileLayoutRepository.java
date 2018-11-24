package com.fpt.capstone.wcs.repository;


import com.fpt.capstone.wcs.model.entity.MobileLayoutReport;
import com.fpt.capstone.wcs.model.entity.PageOption;
import com.fpt.capstone.wcs.model.entity.SpeedTestReport;
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
