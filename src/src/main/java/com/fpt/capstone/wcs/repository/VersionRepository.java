package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.Version;
import com.fpt.capstone.wcs.model.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionRepository extends JpaRepository<Version,Long> {
    Version findVersionByWebsite(Website web);
    Version findVersionByWebsiteAndVersion(Website web, int version);
}
