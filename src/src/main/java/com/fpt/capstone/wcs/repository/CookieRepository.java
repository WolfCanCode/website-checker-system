package com.fpt.capstone.wcs.repository;
import com.fpt.capstone.wcs.model.Cookie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CookieRepository extends JpaRepository<Cookie,Long> {
}
