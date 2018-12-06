package com.fpt.capstone.wcs.repository.website;

import com.fpt.capstone.wcs.model.entity.website.Page;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import com.fpt.capstone.wcs.model.entity.website.Version;
import com.fpt.capstone.wcs.model.entity.user.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page,Long> {
    List<Page> findAllByWebsiteAndVersionAndPageOptions(Website website, Version version, PageOption pageOption);
    List<Page> findAllByWebsite(Website website);
    List<Page> findAllByWebsiteAndVersionAndTypeEquals(Website website, Version version, int type);
    List<Page> findAllByVersion(Version version);
    Page findAllByWebsiteAndVersionAndUrlEquals(Website website,Version version, String url);
}
