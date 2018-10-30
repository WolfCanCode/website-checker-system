package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionRepository extends JpaRepository<Version,Long> {
}
