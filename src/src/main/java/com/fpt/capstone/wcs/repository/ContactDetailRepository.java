package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.ContactReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactDetailRepository extends JpaRepository<ContactReport,Long> {
}
