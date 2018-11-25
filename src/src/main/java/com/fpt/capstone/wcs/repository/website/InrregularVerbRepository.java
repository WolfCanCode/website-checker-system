package com.fpt.capstone.wcs.repository.website;

import com.fpt.capstone.wcs.model.entity.website.InrregularVerb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface InrregularVerbRepository extends JpaRepository<InrregularVerb,Long> {
}
