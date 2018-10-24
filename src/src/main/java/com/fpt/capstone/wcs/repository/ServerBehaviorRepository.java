package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.ServerBehaviorReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerBehaviorRepository extends JpaRepository<ServerBehaviorReport,Long> {
}
