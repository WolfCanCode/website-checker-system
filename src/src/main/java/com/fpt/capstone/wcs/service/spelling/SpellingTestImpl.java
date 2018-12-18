package com.fpt.capstone.wcs.service.spelling;

import com.fpt.capstone.wcs.model.entity.report.quality.SpellingReport;
import com.fpt.capstone.wcs.model.entity.user.User;
import com.fpt.capstone.wcs.model.entity.user.Website;
import com.fpt.capstone.wcs.model.entity.website.EnglishDictionary;
import com.fpt.capstone.wcs.model.entity.website.Page;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import com.fpt.capstone.wcs.model.pojo.*;
import com.fpt.capstone.wcs.repository.report.quality.SpellingTestResultRepository;
import com.fpt.capstone.wcs.repository.user.WebsiteRepository;
import com.fpt.capstone.wcs.repository.website.PageOptionRepository;
import com.fpt.capstone.wcs.service.system.authenticate.AuthenticateService;
import com.fpt.capstone.wcs.service.system.trietree.TrieService;
import com.fpt.capstone.wcs.utils.Constant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class SpellingTestImpl implements SpellingTestService {

    final PageOptionRepository pageOptionRepository;
    final AuthenticateService authenticate;
    final SpellingTestResultRepository spellingTestResultRepository;
    final TrieService trieService;
    final WebsiteRepository webRepo;


    @Autowired
    public SpellingTestImpl(PageOptionRepository pageOptionRepository, AuthenticateService authenticate, SpellingTestResultRepository spellingTestResultRepository, TrieService trieService, WebsiteRepository webRepo) {
        this.pageOptionRepository = pageOptionRepository;
        this.authenticate = authenticate;
        this.spellingTestResultRepository = spellingTestResultRepository;
        this.trieService = trieService;
        this.webRepo = webRepo;
    }


    @Override
    public Map<String, Object> loadPreviousSpellingTestResult(RequestCommonPOJO request) {
        Map<String, Object> res = new HashMap<>();
        Website website = authenticate.isAuthGetSingleSite(request);
        if (website != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), website, false);
            if (pageOption == null) {
                request.setPageOptionId((long) -1);
            }

            if (request.getPageOptionId() != -1) {
                SpellingReport spellingReport = spellingTestResultRepository.findFirstByPageOptionAndDelFlagEqualsOrderByCreatedTimeDesc(pageOption, false);
                if (spellingReport != null) {
                    Date lastedCreatedTime = spellingReport.getCreatedTime();
                    List<SpellingReport> resultList = spellingTestResultRepository.findAllByPageOptionAndCreatedTime(pageOption, lastedCreatedTime);
                    res.put("spellingReport", resultList);
                    res.put("action", Constant.SUCCESS);
                    return res;
                } else {
                    List<SpellingReport> resultList = spellingTestResultRepository.findAllByPageOptionAndAndPage(null, website.getUrl());
                    res.put("spellingReport", resultList);
                    res.put("action", Constant.SUCCESS);
                    return res;
                }

            } else {
                List<SpellingReport> resultList = spellingTestResultRepository.findAllByPageOptionAndAndPage(null, website.getUrl());
                res.put("spellingReport", resultList);
                res.put("action", Constant.SUCCESS);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> getSpellingMistakes(RequestCommonPOJO request, TrieService trieService) {
        Map<String, Object> res = new HashMap<>();
        WebsiteUserPOJO userWebsite = authenticate.isAuthGetUserAndWebsite(request);
        User user = authenticate.isAuthGetSingleUser(request);
        if (userWebsite != null) {
            PageOption pageOption = pageOptionRepository.findOneByIdAndWebsiteAndDelFlagEquals(request.getPageOptionId(), userWebsite.getWebsite(), false);
            if (pageOption == null) {
                request.setPageOptionId((long) -1);
            }

            if (request.getPageOptionId() == -1) { // only test on url root

                List<Page> pages = new ArrayList<>();
                Page page = new Page();
                page.setUrl(userWebsite.getWebsite().getUrl());
                page.setType(1);
                pages.add(page);

                List<SpellingReport> rs = testSpelling(pages, pageOption, trieService);
                spellingTestResultRepository.saveAll(rs);
                res.put("action", Constant.SUCCESS);
                res.put("spellingReport", rs);
                return res;
            } else {
                List<SpellingReport> rs = testSpelling(pageOption.getPages(),pageOption, trieService);
                spellingTestResultRepository.saveAll(rs);
                res.put("action", Constant.SUCCESS);
                res.put("spellingReport", rs);
                return res;
            }


        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    @Override
    public Map<String, Object> getSuggestion(SpellingSuggestionRequestPOJO request, TrieService trieService) {
        Map<String, Object> rs = new HashMap<>();
        List<String> sgStrList = trieService.getSuggestList(request.getWrongWord());
        List<SuggestionPOJO> sgObjList = new ArrayList<>();
        for (String suggestWord : sgStrList) {
            SuggestionPOJO suggest = new SuggestionPOJO();
            suggestWord = suggestWord.substring(0, 1).toUpperCase()+suggestWord.substring(1);
            EnglishDictionary en_word = trieService.getDictionaryRepository().findByWord(suggestWord).get(0);

            suggest.setSuggestWord(en_word.getWord());
            suggest.setType((en_word.getType()));
            String def = en_word.getDefinition();
            String[] subDef = def.split("[0-9]");

            String newDef = "";
            int maxLen = 200;
            int totLen = 0;
            int cntMean = 0;

            for (String mean : subDef) {
                if (mean.length() > 0) {
                   cntMean++;
                   if (mean.length() > 50) mean =  mean.substring(0, 50) + "...";
                   newDef = newDef + (cntMean) + ") " + mean.substring(1) + " ";
                }
                if (cntMean >= 3) break;
            }

            suggest.setDefinition(newDef);
            sgObjList.add(suggest);
        }
        rs.put("action", Constant.SUCCESS);
        rs.put("suggestObjectList", sgObjList);
        return rs;
    }

    @Override
    public Map<String, Object> saveSpellingTestResult(RequestReportPOJO request) {

        Map<String, Object> res = new HashMap<>();
        RequestCommonPOJO requestCommon = new RequestCommonPOJO();
        requestCommon.setPageOptionId(request.getPageOptionId());
        requestCommon.setUserId(request.getUserId());
        requestCommon.setWebsiteId(request.getWebsiteId());
        requestCommon.setUserToken(request.getUserToken());
        WebsiteUserPOJO userWebsite = authenticate.isAuthGetUserAndWebsite(requestCommon);
        if (userWebsite != null) {

            List<SpellingReport> listReport = new ArrayList<>();
            for (int i = 0; i < request.getListReportId().size(); i++) {
                Optional<SpellingReport> optionalReport = spellingTestResultRepository.findById(request.getListReportId().get(i));
                if (optionalReport.isPresent()) {
                    SpellingReport report = optionalReport.get();
                    report.setDelFlag(false);
                    listReport.add(report);
                }
            }
            List<SpellingReport> results = spellingTestResultRepository.saveAll(listReport);
            if (results.size() != 0) {
                res.put("action", Constant.SUCCESS);
                res.put("spellingSaveResult", results);
                return res;
            } else {
                res.put("action", Constant.INCORRECT);
                return res;
            }
        } else {
            res.put("action", Constant.INCORRECT);
            return res;
        }
    }

    private boolean checkExistSpecial(String word, TrieService trieService) {

        if (trieService.isExistInDictionary(word)) return  true;

        int sz = word.length();

        if (sz > 1 && word.charAt(word.length() - 1) == 's') {
            if (trieService.isExistInDictionary(word.substring(0, sz - 1))) return true;
        }

        if (sz > 2 && word.charAt(sz - 2) == 'e' && word.charAt(sz - 1) == 's') {
            if (trieService.isExistInDictionary(word.substring(0, sz - 2))) return true;
        }

        if (sz > 2 && word.charAt(sz - 2) == 'e' && word.charAt(sz - 1) == 'd') {
            if (trieService.isExistInDictionary(word.substring(0, sz - 2))) return true;
        }

        return  false;
    }

    @Override
    public List<SpellingReport> testSpelling(List<Page> pages, PageOption pageOption, TrieService trieService) {
        List<SpellingReport> spellingReportList = new ArrayList<>();
        Map<String, List<ContentPOJO>> rsMap = getContent(pages);
        for (Map.Entry<String, List<ContentPOJO>> entry : rsMap.entrySet()) {
            String page = entry.getKey();
            List<ContentPOJO> contentPOJOS = entry.getValue();

            for (ContentPOJO content : contentPOJOS) {
                String origin = content.getValue();
                String excerp = content.getValue().toLowerCase() + " ";

                String curWord = "";
                int l = -1, r = -1;

                for (int i = 0; i < excerp.length(); i++) {
                    if (excerp.charAt(i) >= 'a' && excerp.charAt(i) <= 'z') {
                        if (curWord.length() == 0) l = i;
                        curWord = curWord + excerp.charAt(i);
                        r = i;
                    } else {
                        // process
                        if (l != -1 && curWord.length() > 0) {
                            if (!checkExistSpecial(curWord, trieService)) {
                                List<String> sgWords = trieService.getSuggestList(curWord);
                                String customize = origin.substring(0, l) + "[" + origin.substring(l, r + 1) + "]" + origin.substring(r + 1);
                                System.out.println("************* Length: " + customize.length());
                                SpellingReport spellingReport = new SpellingReport();
                                spellingReport.setCreatedTime(new Date());
                                spellingReport.setExcerpt(customize);
                                spellingReport.setPage(page);
                                spellingReport.setWrongWord(origin.substring(l, r + 1));
                                spellingReport.setPageOption(pageOption);
                                spellingReportList.add(spellingReport);
                            }
                        }
                        l = -1;
                        r = -1;
                        curWord = "";
                    }
                }
            }
        }
        return spellingReportList;
    }

    private Map<String, List<ContentPOJO>> getContent(List<Page> pages) {
        String currUrl = "";
        String content = "";
        Map<String, List<ContentPOJO>> result = new HashMap<>();
        for (Page p : pages) {
            currUrl = p.getUrl().trim();
            List<ContentPOJO> list = new ArrayList<>();
            try {
                Document doc = Jsoup.connect(currUrl).get();
                Elements elms = doc.getAllElements();
                for (Element elm : elms) {
                    if (elm.tag().toString().equals("a")) {
                        String val = elm.select("a[href]").text();
                        if(val.length()>254){
                            val = val.substring(0, 245);
                        }
                        ContentPOJO ct = new ContentPOJO("aHref", val);
                        
                        list.add(ct);
                        
                    } else if (elm.tag().toString().equals("option")) {
                        String val = elm.select("a[href]").text();
                        if(val.length()>245){
                            val = val.substring(0, 245);
                        }
                        ContentPOJO ct = new ContentPOJO("aHref", val);
                        list.add(ct);
                    } else {
                        if (!elm.tag().toString().equals("body")) {
                            List<TextNode> texts = elm.textNodes();
                            for (TextNode textNode : texts) {
                                String ct = textNode.text();
                                if(ct.length()>245){
                                    ct    = ct.substring(0, 245);
                                }
                                list.add(new ContentPOJO(elm.tag().toString(), ct));
                            }
                        }
                    }
                }
                result.put(currUrl, list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

}
