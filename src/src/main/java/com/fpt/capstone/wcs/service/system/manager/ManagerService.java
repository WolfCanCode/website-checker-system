package com.fpt.capstone.wcs.service.system.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fpt.capstone.wcs.model.pojo.GuestPOJO;
import com.fpt.capstone.wcs.model.pojo.ManagerRequestPOJO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.MalformedURLException;
import java.util.Map;

@Service
public interface ManagerService {
    public Map<String, Object> getmanageWesite(ManagerRequestPOJO request);
    public Map<String, Object> addWebsite(ManagerRequestPOJO request) throws MalformedURLException, JsonProcessingException;
    public Map<String, Object> editWebsite(ManagerRequestPOJO request);
    public Map<String, Object> delWebsite(ManagerRequestPOJO request);
    public Map<String, Object> assignWebsite(ManagerRequestPOJO request);
    public Map<String, Object> defaultAssWebsite(ManagerRequestPOJO request);
    public Map<String, Object> getAllStaff(ManagerRequestPOJO request);
    public Map<String, Object> addStaff(ManagerRequestPOJO request);
    public Map<String, Object> editStaff(ManagerRequestPOJO request);
    public Map<String, Object> deleteStaff(ManagerRequestPOJO request);
    public Map<String, Object> getWarningWordList(ManagerRequestPOJO request);
    public Map<String, Object> getTopicList(ManagerRequestPOJO request);
    public Map<String, Object> addWarningWord(ManagerRequestPOJO request) throws MalformedURLException;
    public Map<String, Object> editWarningWord(ManagerRequestPOJO request);
    public Map<String, Object> deleteWarningWord(ManagerRequestPOJO request);

    //dashboard
    public Map<String, Object> getReportDetail(ManagerRequestPOJO request);
    public Map<String, Object> getAllReport(ManagerRequestPOJO request);

    //guest
    public Map<String, Object> autoguest(GuestPOJO request) throws MalformedURLException, InterruptedException;
    public Map<String, Object> getGuestBrokenLink(GuestPOJO request) ;
    public Map<String, Object> getGuestMissingFile(GuestPOJO request);

}
