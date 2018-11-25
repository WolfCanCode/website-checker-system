package com.fpt.capstone.wcs.repository.website;

import com.fpt.capstone.wcs.model.entity.website.Version;
import com.fpt.capstone.wcs.model.entity.user.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionRepository extends JpaRepository<Version,Long> {
    Version findFirstByWebsiteOrderByVersionDesc(Website web);
    Version findVersionByWebsiteAndVersion(Website web, int version);
}
