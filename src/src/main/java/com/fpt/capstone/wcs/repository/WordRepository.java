package com.fpt.capstone.wcs.repository;

import com.fpt.capstone.wcs.model.entity.User;
import com.fpt.capstone.wcs.model.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word,Long> {
    List<Word> findAllByUserAndDelFlagEquals(User user, boolean isDel);
    List<Word> findAllByDelFlagEquals(boolean isDel);

   Word findOneByUserAndIdAndDelFlagEquals(User user, Long id, boolean isDel);
    Word findFirstByUserAndWordAndDelFlagEquals(User manager , String word, boolean isDel);
//    Website findOneByUserAndUrlAndDelFlagEquals(User user, String url, boolean isDel);
}
