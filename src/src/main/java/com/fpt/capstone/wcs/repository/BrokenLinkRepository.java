package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.BrokenLinkReport;
import com.fpt.capstone.wcs.model.entity.PageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BrokenLinkRepository extends JpaRepository<BrokenLinkReport,Long> {
    List<BrokenLinkReport> findAllByPageOption(PageOption pageOption);
    List<BrokenLinkReport> findAllByPageOptionAndUrlLink(PageOption pageOption, String url);
    BrokenLinkReport findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(PageOption pageOption, boolean delFlag);
    List<BrokenLinkReport> findAllByPageOptionAndCreatedTime(PageOption pageOption, Date createdTime);
    void removeAllByPageOption(PageOption pageOption);
}
