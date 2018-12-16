package com.fpt.capstone.wcs.repository.report.technology;

import com.fpt.capstone.wcs.model.entity.report.technology.ServerBehaviorReport;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ServerBehaviorRepository extends JpaRepository<ServerBehaviorReport,Long> {
void removeAllByPageOption(PageOption pageOption);
List<ServerBehaviorReport> findAllByPageOptionAndDelFlagEquals(PageOption pageOption, boolean delFlag);
ServerBehaviorReport findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(PageOption pageOption, boolean delFlag);
List<ServerBehaviorReport> findAllByPageOptionAndCreatedTime(PageOption pageOption, Date time);
}
