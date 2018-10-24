package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.ContactReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactDetailRepository extends JpaRepository<ContactReport,Long> {
}
