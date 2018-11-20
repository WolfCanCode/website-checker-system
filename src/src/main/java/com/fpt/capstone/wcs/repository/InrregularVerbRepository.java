package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.CookieData;
import com.fpt.capstone.wcs.model.entity.InrregularVerb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface InrregularVerbRepository extends JpaRepository<InrregularVerb,Long> {
}
