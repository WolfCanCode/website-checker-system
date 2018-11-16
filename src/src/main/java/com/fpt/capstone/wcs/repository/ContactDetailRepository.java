package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.ContactReport;
import com.fpt.capstone.wcs.model.entity.PageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactDetailRepository extends JpaRepository<ContactReport,Long> {
    List<ContactReport> findAllByPageOption(PageOption pageOption);
    List<ContactReport> findAllByPageOptionAndUrl(PageOption pageOption, String url);
    void removeAllByPageOption(PageOption pageOption);
}

