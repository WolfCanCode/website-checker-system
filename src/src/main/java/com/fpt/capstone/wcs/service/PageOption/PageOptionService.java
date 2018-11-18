package com.fpt.capstone.wcs.service.PageOption;

import com.fpt.capstone.wcs.model.pojo.PageOptionPOJO;
import com.fpt.capstone.wcs.model.pojo.RequestCommonPOJO;

import java.util.Map;

public interface PageOptionService {

    public Map<String, Object> init(RequestCommonPOJO request);
    public Map<String, Object> selectPageOption(PageOptionPOJO request);
    public Map<String, Object> addPageOption(PageOptionPOJO request);
    public Map<String, Object> updatePageOption(PageOptionPOJO request);
    public Map<String, Object> updateNamePageOption(PageOptionPOJO request);
    public Map<String, Object> deletePageOption(PageOptionPOJO request);
    public Map<String, Object> pageOptionSize(PageOptionPOJO request);

}
