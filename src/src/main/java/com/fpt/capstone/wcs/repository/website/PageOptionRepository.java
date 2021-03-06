package com.fpt.capstone.wcs.repository.website;

import com.fpt.capstone.wcs.model.entity.website.Page;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import com.fpt.capstone.wcs.model.entity.user.User;
import com.fpt.capstone.wcs.model.entity.user.Website;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageOptionRepository extends JpaRepository<PageOption,Long> {
    PageOption findFirstByWebsiteAndCreatedUserAndDelFlagEqualsOrderByTimeDesc(Website website,User user, boolean delFlag);
    PageOption findFirstByWebsiteAndDelFlagEqualsAndNameEqualsAndCreatedUserOrderByTimeDesc(Website website, boolean delFlag, String name, User createdUser);
    PageOption findByPagesAndDelFlagEquals(List<Page> pages, boolean delFlag);
    List<PageOption> findAllByWebsiteAndCreatedUserAndDelFlagEqualsOrderByTimeDesc(Website website, User user, boolean delFlag);
    List<PageOption> findAllByWebsiteAndCreatedUserAndDelFlagEqualsOrderByTimeAsc(Website website, User user, boolean delFlag);
    List<PageOption> findAllByWebsiteAndDelFlagEquals(Website website, boolean delFlag);
    PageOption findOneByIdAndWebsiteAndDelFlagEquals(Long id, Website website, boolean delFlag);
    PageOption findOneByWebsiteAndNameEqualsAndDelFlagEqualsAndCreatedUser(Website website, String name, boolean delFlag, User user);
    PageOption findOneByName(String name);
}
