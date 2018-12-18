package com.fpt.capstone.wcs.repository.website;

import com.fpt.capstone.wcs.model.entity.website.EnglishDictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnglishDictionaryRepository extends JpaRepository<EnglishDictionary, Long>{
    List<EnglishDictionary> findAll();
    List<EnglishDictionary> findByWord(String word);
}
