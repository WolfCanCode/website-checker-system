package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.PageOption;
import com.fpt.capstone.wcs.model.entity.RedirectionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRedirectionRepository extends JpaRepository<RedirectionReport,Long> {
    List<RedirectionReport> findAllByPageOption(PageOption pageOption);
    List<RedirectionReport> findAllByPageOptionAndUrl(PageOption pageOption, String url);
    void removeAllByPageOption(PageOption pageOption);
}
