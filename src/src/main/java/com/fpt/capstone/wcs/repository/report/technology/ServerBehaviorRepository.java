package com.fpt.capstone.wcs.repository.report.technology;

import com.fpt.capstone.wcs.model.entity.report.technology.ServerBehaviorReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerBehaviorRepository extends JpaRepository<ServerBehaviorReport,Long> {

}
