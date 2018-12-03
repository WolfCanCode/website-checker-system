package com.fpt.capstone.wcs.repository.website;

import com.fpt.capstone.wcs.model.entity.user.Website;
import com.fpt.capstone.wcs.model.entity.website.Sitemap;
import com.fpt.capstone.wcs.model.entity.website.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SitemapRepository extends JpaRepository<Sitemap, Long> {
    public Sitemap findByWebsiteAndVersion(Website website, Version version);
}
