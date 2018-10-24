package com.fpt.capstone.wcs.repository;


import com.fpt.capstone.wcs.model.entity.SpeedTestReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeedtestRepository extends JpaRepository<SpeedTestReport,Long> {

}
