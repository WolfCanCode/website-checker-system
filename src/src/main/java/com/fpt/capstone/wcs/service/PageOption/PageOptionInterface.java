package com.fpt.capstone.wcs.service.PageOption;

import com.fpt.capstone.wcs.model.pojo.PageOptionPojo;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;

import java.util.Map;

public interface PageOptionInterface {

    public Map<String, Object> init(RequestCommonPOJO request);
    public Map<String, Object> selectPageOption(PageOptionPojo request);
    public Map<String, Object> addPageOption(PageOptionPojo request);
    public Map<String, Object> updatePageOption(PageOptionPojo request);
    public Map<String, Object> updateNamePageOption(PageOptionPojo request);
    public Map<String, Object> deletePageOption(PageOptionPojo request);
    public Map<String, Object> pageOptionSize(PageOptionPojo request);

}
