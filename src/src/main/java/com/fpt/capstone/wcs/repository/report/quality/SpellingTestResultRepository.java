package com.fpt.capstone.wcs.repository.report.quality;

import com.fpt.capstone.wcs.model.entity.report.quality.SpellingReport;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpellingTestResultRepository extends JpaRepository<SpellingReport, Long> {
//    public SpellingReport findFirstByByPageOptionAndAndDeAndDelFlagEqualsOrderByCreatedTime(PageOption pageOption, boolean isDel);
//    void removeAllByPageOption(PageOption pageOptions);
}
