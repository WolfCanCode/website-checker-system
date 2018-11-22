package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.Page;
import com.fpt.capstone.wcs.model.entity.PageOption;
import com.fpt.capstone.wcs.model.entity.User;
import com.fpt.capstone.wcs.model.entity.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageOptionRepository extends JpaRepository<PageOption,Long> {
    PageOption findFirstByWebsiteAndDelFlagEqualsOrderByTimeDesc(Website website, boolean delFlag);
    PageOption findFirstByWebsiteAndDelFlagEquals(Website website, boolean delFlag);
    PageOption findByPagesAndDelFlagEquals(List<Page> pages, boolean delFlag);
    List<PageOption> findAllByWebsiteAndCreatedUserAndDelFlagEqualsOrderByTimeDesc(Website website, User user, boolean delFlag);
    List<PageOption> findAllByWebsiteAndDelFlagEquals(Website website, boolean delFlag);
    PageOption findOneByIdAndWebsiteAndDelFlagEquals(Long id, Website website, boolean delFlag);
}
