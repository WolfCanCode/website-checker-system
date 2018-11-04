package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.Page;
import com.fpt.capstone.wcs.model.entity.PageOption;
import com.fpt.capstone.wcs.model.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageOptionRepository extends JpaRepository<PageOption,Long> {
    PageOption findFirstByWebsiteOrderByTimeDesc(Website website);
    PageOption findFirstByWebsite(Website website);
    PageOption findByPages(List<Page> pages);
}
