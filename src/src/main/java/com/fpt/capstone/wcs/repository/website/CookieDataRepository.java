package com.fpt.capstone.wcs.repository.website;

import com.fpt.capstone.wcs.model.entity.website.CookieData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CookieDataRepository extends JpaRepository<CookieData,Long> {
}
