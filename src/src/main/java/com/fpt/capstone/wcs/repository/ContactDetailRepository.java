package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.ContactDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactDetailRepository extends JpaRepository<ContactDetail,Long> {
}
