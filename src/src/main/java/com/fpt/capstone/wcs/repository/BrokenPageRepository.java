package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.BrokenPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrokenPageRepository extends JpaRepository<BrokenPage,Long> {
}
