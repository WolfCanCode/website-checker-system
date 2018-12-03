package com.fpt.capstone.wcs.controller.lab;

import com.fpt.capstone.wcs.model.pojo.LabPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;
import com.fpt.capstone.wcs.service.lab.TrieTree;
import com.fpt.capstone.wcs.utils.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LabController {

    @CrossOrigin
    @PostMapping("/api/lab/spelling")
    public Map<String, Object> getHeaderWebsiteStaff(@RequestBody LabPOJO request) {
        Map<String,Object> res = new HashMap<>();
        res.put("action", Constant.SUCCESS);

        TrieTree trie = new TrieTree();
        res.put("data", request.getParagraph());
        return  res;
    }

}
