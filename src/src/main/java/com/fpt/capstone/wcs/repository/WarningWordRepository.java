package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.User;
import com.fpt.capstone.wcs.model.entity.WarningWord;
import com.fpt.capstone.wcs.model.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarningWordRepository extends JpaRepository<WarningWord,Long> {
    List<WarningWord> findAllByUserAndDelFlagEquals(User user, boolean isDel);
    List<WarningWord> findAllByDelFlagEquals(boolean isDel);

    WarningWord findOneByUserAndIdAndDelFlagEquals(User user, Long id, boolean isDel);
    WarningWord findFirstByUserAndWordAndDelFlagEquals(User manager , String word, boolean isDel);
//    Website findOneByUserAndUrlAndDelFlagEquals(User user, String url, boolean isDel);
}
