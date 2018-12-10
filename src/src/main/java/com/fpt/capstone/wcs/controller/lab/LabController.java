package com.fpt.capstone.wcs.controller.lab;

import com.fpt.capstone.wcs.model.pojo.LabPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.model.pojo.SpellingItemPOJO;
import com.fpt.capstone.wcs.service.lab.TrieTree;
import com.fpt.capstone.wcs.service.system.lab.LabService;
import com.fpt.capstone.wcs.utils.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LabController {
    private TrieTree tt = new TrieTree();

    @PostMapping("/api/lab/initTrie")
    public void init() {
        tt = new TrieTree("C:/Users/ngoct/Downloads/DemoSpelling/src/dictionary_full.txt");
        tt.buildTree();
    }

    @CrossOrigin
    @PostMapping("/api/lab/spelling")
    public Map<String, Object> spellingLab(@RequestBody LabPOJO request) {
        Map<String, Object> res = new HashMap<>();
        List<SpellingItemPOJO> resultData = new ArrayList<>();
        TrieTree trie = tt;
        for (String word : request.getParagraph().split(" ")) {
            SpellingItemPOJO item = new SpellingItemPOJO();
            System.out.println("WORD: " + word + "\n---------------------");
            List<String> suggests = tt.doCallBackAfterInspectFinish(word);
            item.setName(word);
            item.setSuggest(suggests);
            resultData.add(item);
        }
        res.put("action", Constant.SUCCESS);
        res.put("data", resultData);
        return res;
    }


}
