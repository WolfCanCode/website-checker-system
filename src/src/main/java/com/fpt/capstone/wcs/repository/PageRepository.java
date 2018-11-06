package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.Page;
import com.fpt.capstone.wcs.model.entity.PageOption;
import com.fpt.capstone.wcs.model.entity.Version;
import com.fpt.capstone.wcs.model.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page,Long> {
    List<Page> findAllByWebsiteAndVersionAndPageOptions(Website website, Version version, PageOption pageOption);
    List<Page> findAllByWebsite(Website website);
    List<Page> findAllByWebsiteAndVersionAndTypeEquals(Website website, Version version, int type);

}
