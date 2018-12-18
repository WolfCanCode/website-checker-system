package com.fpt.capstone.wcs.service.spelling;

import com.fpt.capstone.wcs.model.entity.report.quality.SpellingReport;
import com.fpt.capstone.wcs.model.entity.user.User;
import com.fpt.capstone.wcs.model.entity.user.Website;
import com.fpt.capstone.wcs.model.entity.website.Page;
import com.fpt.capstone.wcs.model.entity.website.PageOption;
import com.fpt.capstone.wcs.model.pojo.ContentPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.SpellingSuggestionRequestPOJO;
import com.fpt.capstone.wcs.model.pojo.WebsiteUserPOJO;
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
        return null;
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

                List<SpellingReport> rs = testSpelling(pages, trieService);
                res.put("action", Constant.SUCCESS);
                res.put("spellingReport", rs);
                return res;
            } else {
                List<SpellingReport> rs = testSpelling(pageOption.getPages(), trieService);
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
        List<String> sgList = trieService.getSuggestList(request.getWrongWord());
        rs.put("action", Constant.SUCCESS);
        rs.put("suggestList", sgList);
        return rs;

    }

    @Override
    public Map<String, Object> saveSpellingTestResult(RequestCommonPOJO request) {
        return null;
    }

    @Override
    public List<SpellingReport> testSpelling(List<Page> pages, TrieService trieService) {
        List<SpellingReport> spellingReportList = new ArrayList<>();
        Map<Integer, List<ContentPOJO>> rsMap = getContent(pages);
        for (Map.Entry<Integer, List<ContentPOJO>> entry : rsMap.entrySet()) {
            int pageId = entry.getKey();
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
                            if (!trieService.isExistInDictionary(curWord)) {
                                List<String> sgWords = trieService.getSuggestList(curWord);
                                String customize = origin.substring(0, l) + "[" + origin.substring(l, r + 1) + "]" + origin.substring(r + 1);
                                SpellingReport spellingReport = new SpellingReport();
                                spellingReport.setCreatedTime(new Date());
                                spellingReport.setExcerpt(customize);
                                spellingReport.setPageId(pageId);
                                spellingReport.setWrongWord(origin.substring(l, r + 1));

                                spellingReportList.add(spellingReport);

                                System.out.println("word = " + curWord);
                                System.out.println("customize = " + customize);
                                System.out.println("page id = " + pageId);
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

    private Map<Integer, List<ContentPOJO>> getContent(List<Page> pages) {
        String currUrl = "";
        String content = "";
        Map<Integer, List<ContentPOJO>> result = new HashMap<>();
        for (Page p : pages) {
            currUrl = p.getUrl().trim();
            List<ContentPOJO> list = new ArrayList<>();
            try {
                Document doc = Jsoup.connect(currUrl).get();
                Elements elms = doc.getAllElements();
                for (Element elm : elms) {
                    if (elm.tag().toString().equals("a")) {
                        String val = elm.select("a[href]").text();
                        ContentPOJO ct = new ContentPOJO("aHref", val);
                        list.add(ct);
                    } else if (elm.tag().toString().equals("option")) {
                        String val = elm.select("a[href]").text();
                        ContentPOJO ct = new ContentPOJO("aHref", val);
                        list.add(ct);
                    } else {
                        if (!elm.tag().toString().equals("body")) {
                            List<TextNode> texts = elm.textNodes();
                            for (TextNode textNode : texts) {
                                list.add(new ContentPOJO(elm.tag().toString(), textNode.text()));
                            }
                        }
                    }
                }
                result.put((int) p.getId(), list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

}
