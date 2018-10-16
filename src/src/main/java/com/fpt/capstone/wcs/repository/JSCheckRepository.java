package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.JSInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JSCheckRepository extends JpaRepository<JSInfo,Long> {
}
