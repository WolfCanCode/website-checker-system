package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.Cookie;
import com.fpt.capstone.wcs.model.entity.CookieData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CookieDataRepository extends JpaRepository<Cookie,Long> {

}
