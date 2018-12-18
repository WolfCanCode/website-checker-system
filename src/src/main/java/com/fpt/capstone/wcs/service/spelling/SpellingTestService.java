package com.fpt.capstone.wcs.service.spelling;

import com.fpt.capstone.wcs.model.entity.report.quality.SpellingReport;
import com.fpt.capstone.wcs.model.entity.website.Page;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.SpellingSuggestionRequestPOJO;
import com.fpt.capstone.wcs.service.system.trietree.TrieService;

import java.util.List;
import java.util.Map;

public interface SpellingTestService {
    public Map<String, Object> loadPreviousSpellingTestResult(RequestCommonPOJO request);
    public Map<String, Object> getSpellingMistakes(RequestCommonPOJO request, TrieService trieService);
    public Map<String, Object> getSuggestion(SpellingSuggestionRequestPOJO request, TrieService trieService);
    public Map<String, Object> saveSpellingTestResult(RequestCommonPOJO request);
    public List<SpellingReport> testSpelling(List<Page> pages, TrieService trieService);
}
